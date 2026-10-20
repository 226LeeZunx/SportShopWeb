
package com.mycompany.sportshopweb.util;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * EmailService gửi email thông qua Brevo REST API.
 *
 * - Không sử dụng Gmail SMTP.
 * - Gửi email qua HTTPS, phù hợp khi deploy trên Render.
 * - Hỗ trợ gửi đồng bộ và bất đồng bộ.
 * - Giữ nguyên các hàm gửi email để Servlet hiện tại tiếp tục sử dụng.
 *
 * Biến môi trường cần cấu hình:
 * BREVO_API_KEY
 * BREVO_SENDER_EMAIL
 * BREVO_SENDER_NAME (không bắt buộc)
 */
public final class EmailService {

    private static final String BREVO_API_URL =
            "https://api.brevo.com/v3/smtp/email";

    private static final String BREVO_API_KEY =
            System.getenv("BREVO_API_KEY");

    private static final String SENDER_EMAIL =
            System.getenv("BREVO_SENDER_EMAIL");

    private static final String SENDER_NAME =
            getSenderName();

    private static final HttpClient HTTP_CLIENT =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .build();

    private EmailService() {
        // Utility class: không tạo instance.
    }

    private static String getSenderName() {
        String name = System.getenv("BREVO_SENDER_NAME");

        if (name == null || name.isBlank()) {
            return "SportShop";
        }

        return name.trim();
    }

    // =========================================================
    // 1. GỬI EMAIL BẤT ĐỒNG BỘ
    // =========================================================

    /**
     * Gửi email trên thread riêng để không chặn luồng xử lý của website.
     *
     * @param toEmail     Email người nhận
     * @param subject     Tiêu đề email
     * @param htmlContent Nội dung HTML
     */
    public static void sendEmailAsync(
            String toEmail,
            String subject,
            String htmlContent
    ) {
        Thread thread = new Thread(() -> {
            try {
                sendEmailSync(toEmail, subject, htmlContent);
            } catch (MessagingException e) {
                System.err.println(
                        "[EmailService] Không gửi được email tới "
                                + toEmail + ": " + e.getMessage()
                );
                e.printStackTrace();
            }
        }, "SportShop-Email-Sender");

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // 2. GỬI EMAIL ĐỒNG BỘ QUA BREVO API
    // =========================================================

    /**
     * Gửi email HTML qua Brevo REST API.
     *
     * @throws MessagingException khi cấu hình thiếu hoặc API thất bại
     */
    public static void sendEmailSync(
            String toEmail,
            String subject,
            String htmlContent
    ) throws MessagingException {

        if (toEmail == null || toEmail.isBlank()) {
            System.err.println(
                    "[EmailService] Email người nhận đang trống."
            );
            return;
        }

        if (subject == null) {
            subject = "";
        }

        if (htmlContent == null) {
            htmlContent = "";
        }

        // Kiểm tra cấu hình trước khi gọi API.
        if (BREVO_API_KEY == null || BREVO_API_KEY.isBlank()) {
            throw new MessagingException(
                    "Thiếu biến môi trường BREVO_API_KEY trên Render."
            );
        }

        if (SENDER_EMAIL == null || SENDER_EMAIL.isBlank()) {
            throw new MessagingException(
                    "Thiếu biến môi trường BREVO_SENDER_EMAIL trên Render."
            );
        }

        // Tạo JSON request body.
        String jsonBody = "{"
                + "\"sender\":{"
                + "\"name\":\"" + jsonEscape(SENDER_NAME) + "\","
                + "\"email\":\"" + jsonEscape(SENDER_EMAIL) + "\""
                + "},"
                + "\"to\":[{"
                + "\"email\":\"" + jsonEscape(toEmail.trim()) + "\""
                + "}],"
                + "\"subject\":\"" + jsonEscape(subject) + "\","
                + "\"htmlContent\":\"" + jsonEscape(htmlContent) + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BREVO_API_URL))
                .timeout(Duration.ofSeconds(30))
                .header("accept", "application/json")
                .header("api-key", BREVO_API_KEY)
                .header("content-type", "application/json")
                .POST(
                        HttpRequest.BodyPublishers.ofString(
                                jsonBody,
                                StandardCharsets.UTF_8
                        )
                )
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(
                            StandardCharsets.UTF_8
                    )
            );

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode >= 200 && statusCode < 300) {
                System.out.println(
                        "[EmailService] Brevo đã chấp nhận yêu cầu gửi email tới: "
                                + toEmail
                                + " | HTTP " + statusCode
                );
                return;
            }

            // Không ghi API key vào log.
            System.err.println(
                    "[EmailService] Brevo API trả về lỗi HTTP "
                            + statusCode
                            + " | Response: " + responseBody
            );

            throw new MessagingException(
                    "Brevo API thất bại. HTTP "
                            + statusCode + ": " + responseBody
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new MessagingException(
                    "Yêu cầu gửi email bị gián đoạn.", e
            );

        } catch (IOException e) {
            throw new MessagingException(
                    "Không kết nối được Brevo API qua HTTPS.", e
            );
        }
    }

    // =========================================================
    // 3. ESCAPE JSON
    // =========================================================

    /**
     * Escape chuỗi Java để tạo JSON hợp lệ.
     */
    private static String jsonEscape(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;

                case '\\':
                    result.append("\\\\");
                    break;

                case '\b':
                    result.append("\\b");
                    break;

                case '\f':
                    result.append("\\f");
                    break;

                case '\n':
                    result.append("\\n");
                    break;

                case '\r':
                    result.append("\\r");
                    break;

                case '\t':
                    result.append("\\t");
                    break;

                default:
                    if (c < 0x20) {
                        result.append(
                                String.format("\\u%04x", (int) c)
                        );
                    } else {
                        result.append(c);
                    }
                    break;
            }
        }

        return result.toString();
    }

    // =========================================================
    // 4. ESCAPE HTML
    // =========================================================

    /**
     * Escape dữ liệu người dùng trước khi chèn vào HTML.
     */
    private static String htmlEscape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // =========================================================
    // 5. EMAIL CHÀO MỪNG
    // =========================================================

    /**
     * Email chào mừng người dùng đăng ký/đăng nhập thành công.
     */
    public static void sendWelcomeEmail(
            String toEmail,
            String customerName
    ) {
        String safeName = htmlEscape(
                customerName != null && !customerName.isBlank()
                        ? customerName
                        : "Bạn"
        );

        String subject =
                "[SPORTSHOP] Chào mừng bạn gia nhập cộng đồng thể thao!";

        String htmlContent =
                "<div style=\"font-family:Arial,sans-serif;"
                + "max-width:600px;margin:auto;padding:25px;"
                + "border:1px solid #e2e8f0;border-radius:12px;"
                + "background-color:#ffffff;\">"

                + "<div style=\"text-align:center;padding-bottom:20px;"
                + "border-bottom:2px solid #991b1b;\">"
                + "<h1 style=\"color:#0f172a;margin:0;font-size:26px;"
                + "letter-spacing:1px;\">⚡ SPORTSHOP</h1>"
                + "<p style=\"color:#64748b;font-size:13px;"
                + "margin:5px 0 0;\">Authentic. Premium. Performance.</p>"
                + "</div>"

                + "<div style=\"padding:25px 0;\">"
                + "<h2 style=\"color:#1e293b;font-size:19px;\">"
                + "Xin chào " + safeName + ",</h2>"

                + "<p style=\"color:#334155;line-height:1.6;font-size:15px;\">"
                + "Chúc mừng bạn đã đăng ký thành công tài khoản thành viên "
                + "tại <b>SPORTSHOP</b>. Từ nay bạn có thể dễ dàng theo dõi "
                + "đơn hàng, quản lý sổ địa chỉ và nhận các ưu đãi độc quyền."
                + "</p>"

                + "<div style=\"background-color:#f8fafc;"
                + "border-left:4px solid #ef4444;padding:15px;"
                + "margin:20px 0;border-radius:4px;\">"

                + "<p style=\"margin:0;font-size:14px;color:#0f172a;\">"
                + "🎁 <b>Mã giảm giá chào sân dành riêng cho bạn:</b></p>"

                + "<p style=\"margin:8px 0 0;font-size:20px;"
                + "font-weight:bold;color:#991b1b;letter-spacing:2px;\">"
                + "WELCOME10</p>"

                + "<p style=\"margin:5px 0 0;font-size:13px;"
                + "color:#64748b;\">"
                + "Giảm ngay 10% cho đơn hàng đầu tiên của bạn tại SportShop."
                + "</p>"

                + "</div>"

                + "<p style=\"color:#334155;line-height:1.6;font-size:14px;\">"
                + "Nếu bạn có bất kỳ thắc mắc nào, đừng ngần ngại liên hệ "
                + "trợ lý AI hoặc đội ngũ hỗ trợ của chúng tôi.</p>"
                + "</div>"

                + "<div style=\"text-align:center;padding-top:20px;"
                + "border-top:1px solid #f1f5f9;color:#94a3b8;"
                + "font-size:12px;\">"

                + "<p style=\"margin:0;\">"
                + "&copy; 2026 SPORTSHOP. All rights reserved.</p>"

                + "<p style=\"margin:4px 0 0;\">"
                + "Email này được gửi tự động từ hệ thống.</p>"
                + "</div>"

                + "</div>";

        sendEmailAsync(toEmail, subject, htmlContent);
    }

    // =========================================================
    // 6. EMAIL OTP XÁC NHẬN ĐĂNG KÝ
    // =========================================================

    /**
     * Gửi mã OTP xác nhận đăng ký tài khoản.
     * Thời hạn OTP được quản lý tại phần xử lý OTP của ứng dụng.
     */
    public static void sendOtpEmail(
            String toEmail,
            String customerName,
            String otp
    ) {
        String safeName = htmlEscape(
                customerName != null && !customerName.isBlank()
                        ? customerName
                        : "Bạn"
        );

        String safeOtp = htmlEscape(otp);

        String subject =
                "[SPORTSHOP] Mã OTP xác nhận đăng ký tài khoản";

        String htmlContent =
                "<div style=\"font-family:Arial,sans-serif;"
                + "max-width:600px;margin:auto;padding:25px;"
                + "border:1px solid #e2e8f0;border-radius:12px;"
                + "background-color:#ffffff;\">"

                + "<div style=\"text-align:center;padding-bottom:20px;"
                + "border-bottom:2px solid #991b1b;\">"
                + "<h1 style=\"color:#0f172a;margin:0;font-size:26px;"
                + "letter-spacing:1px;\">⚡ SPORTSHOP</h1>"
                + "<p style=\"color:#64748b;font-size:13px;"
                + "margin:5px 0 0;\">Authentic. Premium. Performance.</p>"
                + "</div>"

                + "<div style=\"padding:25px 0;text-align:center;\">"

                + "<h2 style=\"color:#1e293b;font-size:20px;\">"
                + "Xác thực đăng ký tài khoản</h2>"

                + "<p style=\"color:#334155;line-height:1.6;font-size:15px;\">"
                + "Xin chào <b>" + safeName + "</b>,<br>"
                + "Bạn vừa yêu cầu đăng ký tài khoản tại <b>SPORTSHOP</b>. "
                + "Dưới đây là mã xác thực OTP của bạn:</p>"

                + "<div style=\"background-color:#fef2f2;"
                + "border:2px dashed #dc2626;padding:18px;"
                + "margin:25px auto;width:240px;border-radius:10px;\">"

                + "<span style=\"font-size:34px;font-weight:bold;"
                + "letter-spacing:8px;color:#991b1b;\">"
                + safeOtp + "</span>"

                + "</div>"

                + "<p style=\"color:#64748b;font-size:13px;line-height:1.5;\">"
                + "Mã OTP có hiệu lực trong vòng <b>5 phút</b>.<br>"
                + "Tuyệt đối không chia sẻ mã này cho bất kỳ ai "
                + "để đảm bảo an toàn tài khoản.</p>"

                + "</div>"

                + "<div style=\"text-align:center;padding-top:20px;"
                + "border-top:1px solid #f1f5f9;color:#94a3b8;"
                + "font-size:12px;\">"

                + "<p style=\"margin:0;\">"
                + "&copy; 2026 SPORTSHOP. All rights reserved.</p>"

                + "<p style=\"margin:4px 0 0;\">"
                + "Email này được gửi tự động từ hệ thống.</p>"
                + "</div>"

                + "</div>";

        sendEmailAsync(toEmail, subject, htmlContent);
    }
}