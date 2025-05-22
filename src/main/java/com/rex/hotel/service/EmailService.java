package com.rex.hotel.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) throws MessagingException {
        String subject = "Xác thực tài khoản của bạn";
        String confirmationUrl = "http://localhost:8080/auth/verify?token=" + token;

        // HTML email template
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 20px auto;
                        padding: 20px;
                        background-color: #f9f9f9;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    .header {
                        background-color: #4CAF50;
                        color: white;
                        padding: 20px;
                        text-align: center;
                        border-radius: 8px 8px 0 0;
                    }
                    .content {
                        padding: 20px;
                        background-color: white;
                    }
                    .button {
                        display: inline-block;
                        padding: 12px 24px;
                        background-color: #4CAF50;
                        color: white !important;
                        text-decoration: none;
                        border-radius: 4px;
                        margin: 20px 0;
                    }
                    .button:hover {
                        background-color: #45a049;
                    }
                    .footer {
                        text-align: center;
                        color: #777;
                        font-size: 12px;
                        padding: 20px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Xác thực tài khoản</h2>
                    </div>
                    <div class="content">
                        <p>Xin chào,</p>
                        <p>Cảm ơn bạn đã đăng ký! Vui lòng nhấp vào nút dưới đây để xác thực tài khoản của bạn:</p>
                        <a href="%s" class="button">Xác thực ngay</a>
                        <p>Nếu nút trên không hoạt động, bạn có thể sao chép và dán liên kết sau vào trình duyệt:</p>
                        <p><a href="%s">%s</a></p>
                        <p>Liên kết này sẽ hết hạn sau 24 giờ.</p>
                    </div>
                    <div class="footer">
                        <p>Đây là email tự động, vui lòng không trả lời trực tiếp.</p>
                        <p>&copy; 2025 Danh Thái Production. Mọi quyền được bảo lưu.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(confirmationUrl, confirmationUrl, confirmationUrl);

        // Create and send HTML email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(message);
    }
}