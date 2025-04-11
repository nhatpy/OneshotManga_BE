package com.anime_social.services;

import com.anime_social.dto.request.Introspect;
import com.anime_social.dto.request.Logout;
import com.anime_social.dto.request.NewPasswordRequest;
import com.anime_social.dto.request.Register;
import com.anime_social.dto.request.Authenticate;
import com.anime_social.dto.request.ChangePasswordRequest;
import com.anime_social.dto.request.EmailRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.AuthenticateResponse;
import com.anime_social.dto.response.UserResponse;
import com.anime_social.enums.Role;
import com.anime_social.models.User;
import com.anime_social.exception.ErrorCode;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.models.VerifyUserCode;
import com.anime_social.repositories.UserRepository;
import com.anime_social.repositories.VerifyUserCodeRepository;
import com.anime_social.services.interfaces.AuthenticationService;
import com.anime_social.services.interfaces.EmailService;
import com.anime_social.services.interfaces.InvalidatedTokenService;
import com.anime_social.services.interfaces.JwtService;
import com.nimbusds.jose.*;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    VerifyUserCodeRepository verifyUserCodeRepository;
    EmailService emailService;
    InvalidatedTokenService invalidatedTokenService;
    JwtService jwtService;

    @Override
    @CacheEvict(value = "USER_CACHE", key = "'getTopUsers'")
    public AppResponse register(Register registerRequest) throws MessagingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Optional<User> existed_user = userRepository.findByEmail(registerRequest.getEmail());

        if (existed_user.isPresent()) {
            throw new CusRunTimeException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .role(roles)
                .build();
        User result = userRepository.save(user);
        VerifyUserCode verifyUserCode = new VerifyUserCode(result);
        verifyUserCodeRepository.save(verifyUserCode);
        emailService.sendEmail(result.getEmail(), verifyUserCode.getCode());

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Đăng ký thành công")
                .data(UserResponse.toUserResponse(result))
                .build();
    }

    @Override
    public AppResponse login(Authenticate authenticateRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(authenticateRequest.getEmail())
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND));

        if (user.getIsBanned()) {
            throw new CusRunTimeException(ErrorCode.USER_GOT_BAN);
        }

        if (!passwordEncoder.matches(authenticateRequest.getPassword(), user.getPassword())) {
            throw new CusRunTimeException(ErrorCode.PASSWORD_IS_INCORRECT);
        }

        if (!user.getIsVerified()) {
            throw new CusRunTimeException(ErrorCode.USER_NOT_VERIFIED);
        }

        String token = jwtService.generateToken(user);

        AuthenticateResponse authenticateResponse = AuthenticateResponse.builder()
                .token(token)
                .user(UserResponse.toUserResponse(user))
                .build();

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Đăng nhập thành công")
                .data(authenticateResponse)
                .build();
    }

    @Override
    public AppResponse introspect(Introspect introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        try {
            jwtService.verifyToken(token);
        } catch (CusRunTimeException e) {
            throw new CusRunTimeException(ErrorCode.INVALID_TOKEN);
        }
        return AppResponse.builder()
                .message("Token hợp lệ")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public AppResponse logout(Logout request) throws ParseException, JOSEException {
        var signToken = jwtService.verifyToken(request.getToken());

        String jid = signToken.getJWTClaimsSet().getJWTID();

        invalidatedTokenService.addToBlacklistedToken(jid);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Đăng xuất thành công")
                .build();
    }

    @Override
    public void verifyUser(String verifyCode, HttpServletResponse response) throws IOException {
        VerifyUserCode verifyUserCode = verifyUserCodeRepository.findByCode(verifyCode)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.INVALID_VERIFY_CODE));

        String userMail = verifyUserCode.getUser().getEmail();
        User user = userRepository.findByEmail(userMail)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND));

        user.setIsVerified(true);
        userRepository.save(user);

        response.sendRedirect("http://localhost:5173/verify-success");
        // response.sendRedirect("https://anime-social-fe.vercel.app/verify-success");
    }

    @Override
    public AppResponse sendVerifyEmail(EmailRequest emailRequest) throws MessagingException {
        User user = userRepository.findByEmail(emailRequest.getEmail())
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND));

        emailService.sendEmailToResetPassword(user.getEmail(), user.getId());

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Gửi email thành công")
                .build();
    }

    @Override
    public void verifyToReset(String userId, HttpServletResponse response) throws IOException {
        userRepository.findById(userId)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));

        String redirectUrl = "http://localhost:5173/reset-password?id=" + userId;
        // String redirectUrl = "https://anime-social-fe.vercel.app/reset-password?id="
        // + userId;

        response.sendRedirect(redirectUrl);
    }

    @Override
    public AppResponse resetPassword(String userId, ChangePasswordRequest changePasswordRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Đặt lại mật khẩu thành công")
                .build();
    }

    @Override
    public AppResponse changePassword(String userId, NewPasswordRequest newPasswordRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(newPasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new CusRunTimeException(ErrorCode.PASSWORD_IS_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        userRepository.save(user);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Đổi mật khẩu thành công")
                .build();
    }
}