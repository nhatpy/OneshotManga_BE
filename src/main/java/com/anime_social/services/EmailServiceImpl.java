package com.anime_social.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.anime_social.services.interfaces.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Override
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

    @Override
    public void sendEmailToResetPassword(String to, String userId) throws MessagingException {
        String apiUrl = "http://localhost:8080/api/auth/verify-to-reset?id=" + userId;

        String emailContent = "<html>"
                + "<body>"
                + "<h2>Email được gửi để xác nhận đúng là bạn đang thực hiện hành động đổi mật khẩu</h2>"
                + "<p>Nhấn vào nút bên dưới để xác thực tài khoản:</p>"
                + "<a href='" + apiUrl + "' "
                + "style='display:inline-block;background-color:#4CAF50;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;'>"
                + "Xác nhận tài khoản</a>"
                + "</body>"
                + "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.addTo(to);
        helper.setSubject("Anime Social - Xác nhận đổi mật khẩu");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }
}
