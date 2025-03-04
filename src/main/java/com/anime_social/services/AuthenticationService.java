package com.anime_social.services;

import com.anime_social.dto.request.Introspect;
import com.anime_social.dto.request.Logout;
import com.anime_social.dto.request.Register;
import com.anime_social.dto.request.Authenticate;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.AuthenticateResponse;
import com.anime_social.models.User;
import com.anime_social.exception.ErrorCode;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.models.VerifyUserCode;
import com.anime_social.repositorys.UserRepository;
import com.anime_social.repositorys.VerifyUserCodeRepository;
import com.anime_social.util.enums.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    VerifyUserCodeRepository verifyUserCodeRepository;
    EmailService emailService;
    InvalidatedTokenService invalidatedTokenService;
    @NonFinal
    @Value("${JWT_SECRET}")
    String jwtSecret;

    @NonFinal
    @Value("${EXPIRED_TIME}")
    long jwtExpiredTime;

    public AppResponse register(Register registerRequest) throws MessagingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User existed_user = userRepository.findByEmail(registerRequest.getEmail());

        if (existed_user != null) {
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
                .message("Register successfully")
                .data(result)
                .build();
    }

    public AppResponse login(Authenticate authenticateRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(authenticateRequest.getEmail());
        if (user == null) {
            throw new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND);
        }
        if (user.getIsBanned()) {
            return AppResponse.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("User is banned")
                    .build();
        }

        if (!passwordEncoder.matches(authenticateRequest.getPassword(), user.getPassword())) {
            throw new CusRunTimeException(ErrorCode.PASSWORD_IS_INCORRECT);
        }

        if (!user.getIsVerified()) {
            throw new CusRunTimeException(ErrorCode.USER_NOT_VERIFIED);
        }

        String token = generateToken(user);

        AuthenticateResponse authenticateResponse = AuthenticateResponse.builder()
                .expiredTime(jwtExpiredTime)
                .token(token)
                .user(user)
                .build();

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Login successfully")
                .data(authenticateResponse)
                .build();
    }

    public AppResponse introspect(Introspect introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        try {
            verifyToken(token);
        } catch (CusRunTimeException e) {
            throw new CusRunTimeException(ErrorCode.INVALID_TOKEN);
        }
        return AppResponse.builder()
                .message("Valid token")
                .status(HttpStatus.OK)
                .build();
    }

    public AppResponse logout(Logout request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jid = signToken.getJWTClaimsSet().getJWTID();

        invalidatedTokenService.addToBlacklistedToken(jid);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Logout successfully")
                .build();
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("anime_social")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiredTime))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildRoleClaim(user))
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

        try {
            JWSSigner signer = new MACSigner(jwtSecret.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing the token", e);
        }

        return signedJWT.serialize();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(jwtSecret.getBytes(StandardCharsets.UTF_8));

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isValid = signedJWT.verify(jwsVerifier);

        if (!(isValid && expirationTime.after(new Date()))) {
            throw new CusRunTimeException(ErrorCode.INVALID_TOKEN);
        }
        if (invalidatedTokenService.isBlacklistedToken(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new CusRunTimeException(ErrorCode.INVALID_TOKEN);
        }

        return signedJWT;
    }

    private String buildRoleClaim(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRole())) {
            user.getRole().forEach(joiner::add);
        }
        return joiner.toString();
    }

    public void verifyUser(String verifyCode, HttpServletResponse response) throws IOException {
        VerifyUserCode verifyUserCode = verifyUserCodeRepository.findByCode(verifyCode);
        if (verifyUserCode == null) {
            throw new CusRunTimeException(ErrorCode.INVALID_VERIFY_CODE);
        }

        String userMail = verifyUserCode.getUser().getEmail();
        User user = userRepository.findByEmail(userMail);
        if (user == null) {
            throw new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND);
        }

        user.setIsVerified(true);
        userRepository.save(user);

        response.sendRedirect("http://localhost:5173/verify-success");
    }

}
