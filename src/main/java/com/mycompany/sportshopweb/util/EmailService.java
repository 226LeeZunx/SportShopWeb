
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
}
