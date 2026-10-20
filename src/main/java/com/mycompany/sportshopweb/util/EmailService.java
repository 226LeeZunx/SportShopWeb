
package com.mycompany.sportshopweb.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp-relay.brevo.com";
    private static final String SMTP_PORT = "587";

    private static String getEnv(String name) {
        String value = System.getenv(name);
        return value == null ? "" : value.trim();
    }

    public static void sendEmailSync(
            String to,
            String subject,
            String body
    ) throws Exception {

        String username = getEnv("BREVO_SMTP_LOGIN");
        String smtpKey = getEnv("BREVO_SMTP_KEY");
        String senderEmail = getEnv("BREVO_SENDER_EMAIL");
        String senderName = getEnv("BREVO_SENDER_NAME");

        if (username.isEmpty()
                || smtpKey.isEmpty()
                || senderEmail.isEmpty()) {
            throw new IllegalStateException(
                "Missing Brevo SMTP environment variables"
            );
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");

        Session session = Session.getInstance(
            props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication
                        getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        username, smtpKey
                    );
                }
            }
        );

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(
            senderEmail,
            senderName.isEmpty() ? "SportShop" : senderName,
            StandardCharsets.UTF_8.name()
        ));
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(to)
        );
        message.setSubject(subject, StandardCharsets.UTF_8.name());
        message.setText(
            body,
            StandardCharsets.UTF_8.name(),
            "html"
        );

        Transport.send(message);
        System.out.println(
            "[EmailService] SMTP gửi email thành công tới: " + to
        );
    }

    public static void sendEmailAsync(
            String to,
            String subject,
            String body
    ) {
        Thread thread = new Thread(() -> {
            try {
                sendEmailSync(to, subject, body);
            } catch (Exception e) {
                System.err.println(
                    "[EmailService] Không gửi được email tới "
                    + to + ": " + e.getMessage()
                );
                e.printStackTrace();
            }
        });

        thread.setName("sportshop-email-sender");
        thread.start();
    }
    public static void sendOtpEmail(
        String to,
        String otp,
        String purpose
) {
    String subject = "SportShop - Ma xac thuc OTP";

    String body = """
        <html>
        <body>
            <h2>SportShop - Xac thuc tai khoan</h2>
            <p>Ma OTP cua ban la:</p>
            <h1 style="color:blue;">%s</h1>
            <p>Muc dich: %s</p>
            <p>Vui long khong chia se ma OTP nay voi nguoi khac.</p>
        </body>
        </html>
        """.formatted(otp, purpose);

    sendEmailAsync(to, subject, body);
}
    public static void sendWelcomeEmail(String to, String name) {
    String subject = "Chao mung ban den voi SportShop!";

    String safeName = name == null || name.isBlank()
            ? "ban"
            : name;

    String body = """
        <html>
        <body>
            <h2>Chao mung den voi SportShop!</h2>
            <p>Xin chao %s,</p>
            <p>Tai khoan cua ban da duoc xac minh thanh cong.</p>
            <p>Cam on ban da tham gia SportShop!</p>
        </body>
        </html>
        """.formatted(safeName);

    sendEmailAsync(to, subject, body);
}
}
