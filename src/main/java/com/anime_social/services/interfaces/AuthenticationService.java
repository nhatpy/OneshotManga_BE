package com.anime_social.services.interfaces;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.Authenticate;
import com.anime_social.dto.request.ChangePasswordRequest;
import com.anime_social.dto.request.EmailRequest;
import com.anime_social.dto.request.Introspect;
import com.anime_social.dto.request.Logout;
import com.anime_social.dto.request.NewPasswordRequest;
import com.anime_social.dto.request.Register;
import com.anime_social.dto.response.AppResponse;
import com.nimbusds.jose.JOSEException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

@Service
public interface AuthenticationService {
    public AppResponse register(Register registerRequest) throws MessagingException;

    public AppResponse login(Authenticate authenticateRequest);

    public AppResponse introspect(Introspect introspectRequest) throws JOSEException, ParseException;

    public AppResponse logout(Logout request) throws ParseException, JOSEException;

    public void verifyUser(String verifyCode, HttpServletResponse response) throws IOException;

    public AppResponse sendVerifyEmail(EmailRequest emailRequest) throws MessagingException;

    public void verifyToReset(String userId, HttpServletResponse response) throws IOException;

    public AppResponse resetPassword(String userId, ChangePasswordRequest changePasswordRequest);

    public AppResponse changePassword(String userId, NewPasswordRequest newPasswordRequest);
}