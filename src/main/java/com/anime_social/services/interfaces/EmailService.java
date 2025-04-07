package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public interface EmailService {
    public void sendEmail(String to, String verificationCode) throws MessagingException;

    public void sendEmailToResetPassword(String to, String userId) throws MessagingException;
}
