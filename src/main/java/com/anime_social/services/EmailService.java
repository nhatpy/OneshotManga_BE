package com.anime_social.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String verificationCode) throws MessagingException {
        String apiUrl = "http://localhost:8080/api/auth/verify-user?code=" + verificationCode;

        String emailContent = "<html>"
                + "<body>"
                + "<h2>Vui lòng xác nhận tài khoản của bạn</h2>"
                + "<p>Nhấn vào nút bên dưới để xác thực tài khoản:</p>"
                + "<a href='" + apiUrl + "' "
                + "style='display:inline-block;background-color:#4CAF50;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;'>"
                + "Xác nhận tài khoản</a>"
                + "</body>"
                + "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.addTo(to);
        helper.setSubject("Anime Social - Xác nhận tài khoản");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }
}
