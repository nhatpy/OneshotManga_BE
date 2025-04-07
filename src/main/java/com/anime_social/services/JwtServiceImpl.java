package com.anime_social.services;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.User;
import com.anime_social.services.interfaces.InvalidatedTokenService;
import com.anime_social.services.interfaces.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @NonFinal
    @Value("${JWT_SECRET}")
    String jwtSecret;

    @NonFinal
    @Value("${EXPIRED_TIME}")
    long jwtExpiredTime;

    private final InvalidatedTokenService invalidatedTokenService;

    @Override
    public String generateToken(User user) {
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
            throw new RuntimeException("Không thể tạo token", e);
        }

        return signedJWT.serialize();
    }

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
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

    @Override
    public String buildRoleClaim(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRole())) {
            user.getRole().forEach(joiner::add);
        }
        return joiner.toString();
    }
}
