package com.anime_social.service;

import com.anime_social.dto.request.IntrospectRequest;
import com.anime_social.dto.request.LogoutRequest;
import com.anime_social.dto.request.RegisterRequest;
import com.anime_social.dto.request.AuthenticateRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.AuthenticateResponse;
import com.anime_social.entity.InvalidatedToken;
import com.anime_social.entity.User;
import com.anime_social.exception.ErrorCode;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.repository.InvalidatedTokenRepository;
import com.anime_social.repository.UserRepository;
import com.anime_social.util.enums.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${JWT_SECRET}")
    String jwtSecret;

    @NonFinal
    @Value("${EXPIRED_TIME}")
    long jwtExpiredTime;

    public AppResponse register(RegisterRequest registerRequest) {
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
        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Register successfully")
                .data(result)
                .build();
    }

    public AppResponse login(AuthenticateRequest authenticateRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(authenticateRequest.getEmail());
        if (user == null) {
            throw new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND);
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
    public AppResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
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
    public AppResponse logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jid = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jid)
                .expiryTime(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

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
        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new CusRunTimeException(ErrorCode.INVALID_TOKEN);

        return signedJWT;
    }

    private String buildRoleClaim(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRole())) {
            user.getRole().forEach(joiner::add);
        }
        return joiner.toString();
    }
}
