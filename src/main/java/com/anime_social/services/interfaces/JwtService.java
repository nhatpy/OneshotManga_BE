package com.anime_social.services.interfaces;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.anime_social.models.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

@Service
public interface JwtService {
    String generateToken(User user);

    SignedJWT verifyToken(String token) throws JOSEException, ParseException;

    String buildRoleClaim(User user);
}
