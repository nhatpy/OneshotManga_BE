package com.anime_social.controller;

import com.anime_social.dto.request.IntrospectRequest;
import com.anime_social.dto.request.LogoutRequest;
import com.anime_social.dto.request.RegisterRequest;
import com.anime_social.dto.request.AuthenticateRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
@RestControllerAdvice
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/register")
    public AppResponse register(@RequestBody @Valid RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public AppResponse login(@RequestBody @Valid AuthenticateRequest authenticateRequest) {
        return authenticationService.login(authenticateRequest);
    }

    @PostMapping("/verify-token")
    public AppResponse verifyToken(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return authenticationService.introspect(introspectRequest);
    }
    @PostMapping("/logout")
    public AppResponse logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        return authenticationService.logout(request);
    }
}
