package com.mycompany.sportshopweb.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service gửi Email thực tế qua máy chủ SMTP của Google Gmail (Dùng Jakarta
 * Mail).
 * Chuẩn mô hình MVC và chạy ngầm (asynchronous thread) để không làm chậm
 * website.
 */
public class EmailService {

    // =========================================================================
    // 👉 CẤU HÌNH GMAIL CỬA HÀNG ĐỂ GỬI ĐI (BẠN ĐIỀN EMAIL & APP PASSWORD VÀO ĐÂY)
    // =========================================================================
    // 1. Điền địa chỉ Gmail gửi thư của bạn:
    private static final String SENDER_EMAIL = "hlat10901@gmail.com";

    // 2. Mật khẩu ứng dụng (App Password 16 chữ cái tạo trong tài khoản Google
    // Security):
    private static final String APP_PASSWORD = "uihw rntw pxdi aaua";

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    /**
     * Gửi Email HTML bất đồng bộ (chạy ngầm trên Thread riêng để không giật lag
     * web)
     * 
     * @param toEmail     Địa chỉ email người nhận
     * @param subject     Tiêu đề thư
     * @param htmlContent Nội dung thư định dạng HTML
     */
    public static void sendEmailAsync(String toEmail, String subject, String htmlContent) {
        new Thread(() -> {
            try {
                sendEmailSync(toEmail, subject, htmlContent);
            } catch (Exception e) {
                System.err.println("[EmailService] Lỗi khi gửi email đến " + toEmail + ": " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Hàm gửi email đồng bộ trực tiếp qua SMTP Gmail
     */
    public static void sendEmailSync(String toEmail, String subject, String htmlContent) throws MessagingException {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        // Xác thực tài khoản gửi thư
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=UTF-8");

        Transport.send(message);
        System.out.println("[EmailService] Đã gửi thành công email tới: " + toEmail);
    }

    /**
     * Mẫu Email chào mừng thành viên đăng nhập bằng Google thành công
     */
    public static void sendWelcomeEmail(String toEmail, String customerName) {
        String safeName = (customerName != null && !customerName.isEmpty()) ? customerName : "Bạn";
        String subject = "[SPORTSHOP] Chào mừng bạn gia nhập cộng đồng thể thao!";

        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 25px; border: 1px solid #e2e8f0; border-radius: 12px; background-color: #ffffff;\">"
                + "<div style=\"text-align: center; padding-bottom: 20px; border-bottom: 2px solid #991b1b;\">"
                + "  <h1 style=\"color: #0f172a; margin: 0; font-size: 26px; letter-spacing: 1px;\">⚡ SPORTSHOP</h1>"
                + "  <p style=\"color: #64748b; font-size: 13px; margin: 5px 0 0;\">Authentic. Premium. Performance.</p>"
                + "<div style=\"padding: 25px 0;\">"
                + "  <h2 style=\"color: #1e293b; font-size: 19px;\">Xin chào " + safeName + ",</h2>"
                + "  <p style=\"color: #334155; line-height: 1.6; font-size: 15px;\">Chúc mừng bạn đã đăng ký thành công tài khoản thành viên tại <b>SPORTSHOP</b>. Từ nay bạn có thể dễ dàng theo dõi đơn hàng, quản lý sổ địa chỉ và nhận các ưu đãi độc quyền.</p>"
                + "  <div style=\"background-color: #f8fafc; border-left: 4px solid #ef4444; padding: 15px; margin: 20px 0; border-radius: 4px;\">"
                + "    <p style=\"margin: 0; font-size: 14px; color: #0f172a;\">🎁 <b>Mã giảm giá chào sân dành riêng cho bạn:</b></p>"
                + "    <p style=\"margin: 8px 0 0; font-size: 20px; font-weight: bold; color: #991b1b; letter-spacing: 2px;\">WELCOME10</p>"
                + "    <p style=\"margin: 5px 0 0; font-size: 13px; color: #64748b;\">Giảm ngay 10% cho đơn hàng đầu tiên của bạn tại SportShop.</p>"
                + "  </div>"
                + "  <p style=\"color: #334155; line-height: 1.6; font-size: 14px;\">Nếu bạn có bất kỳ thắc mắc nào, đừng ngần ngại liên hệ trợ lý AI hoặc đội ngũ hỗ trợ của chúng tôi.</p>"
                + "</div>"
                + "<div style=\"text-align: center; padding-top: 20px; border-top: 1px solid #f1f5f9; color: #94a3b8; font-size: 12px;\">"
                + "  <p style=\"margin: 0;\">&copy; 2026 SPORTSHOP. All rights reserved.</p>"
                + "  <p style=\"margin: 4px 0 0;\">Email này được gửi tự động từ hệ thống.</p>"
                + "</div>"
                + "</div>";

        sendEmailAsync(toEmail, subject, htmlContent);
    }

    /**
     * Mẫu Email gửi mã OTP xác nhận đăng ký tài khoản (Hiệu lực 5 phút)
     */
    public static void sendOtpEmail(String toEmail, String customerName, String otp) {
        String safeName = (customerName != null && !customerName.isEmpty()) ? customerName : "Bạn";
        String subject = "[SPORTSHOP] Mã OTP xác nhận đăng ký tài khoản: " + otp;

        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 25px; border: 1px solid #e2e8f0; border-radius: 12px; background-color: #ffffff;\">"
                + "<div style=\"text-align: center; padding-bottom: 20px; border-bottom: 2px solid #991b1b;\">"
                + "  <h1 style=\"color: #0f172a; margin: 0; font-size: 26px; letter-spacing: 1px;\">⚡ SPORTSHOP</h1>"
                + "  <p style=\"color: #64748b; font-size: 13px; margin: 5px 0 0;\">Authentic. Premium. Performance.</p>"
                + "</div>"
                + "<div style=\"padding: 25px 0; text-align: center;\">"
                + "  <h2 style=\"color: #1e293b; font-size: 20px;\">Xác thực đăng ký tài khoản</h2>"
                + "  <p style=\"color: #334155; line-height: 1.6; font-size: 15px;\">Xin chào <b>" + safeName + "</b>,<br>Bạn vừa yêu cầu đăng ký tài khoản tại <b>SPORTSHOP</b>. Dưới đây là mã xác thực OTP của bạn:</p>"
                + "  <div style=\"background-color: #fef2f2; border: 2px dashed #dc2626; padding: 18px; margin: 25px auto; width: 240px; border-radius: 10px;\">"
                + "    <span style=\"font-size: 34px; font-weight: bold; letter-spacing: 8px; color: #991b1b;\">" + otp + "</span>"
                + "  </div>"
                + "  <p style=\"color: #64748b; font-size: 13px; line-height: 1.5;\">Mã OTP có hiệu lực trong vòng <b>5 phút</b>.<br>Tuyệt đối không chia sẻ mã này cho bất kỳ ai để đảm bảo an toàn tài khoản.</p>"
                + "</div>"
                + "<div style=\"text-align: center; padding-top: 20px; border-top: 1px solid #f1f5f9; color: #94a3b8; font-size: 12px;\">"
                + "  <p style=\"margin: 0;\">&copy; 2026 SPORTSHOP. All rights reserved.</p>"
                + "  <p style=\"margin: 4px 0 0;\">Email này được gửi tự động từ hệ thống.</p>"
                + "</div>"
                + "</div>";

        sendEmailAsync(toEmail, subject, htmlContent);
    }
}
