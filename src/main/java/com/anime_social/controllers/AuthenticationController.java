package com.anime_social.controllers;

import com.anime_social.dto.request.Introspect;
import com.anime_social.dto.request.Logout;
import com.anime_social.dto.request.Register;
import com.anime_social.dto.request.Authenticate;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
@RestControllerAdvice
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public AppResponse register(@RequestBody @Valid Register registerRequest) throws MessagingException {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public AppResponse login(@RequestBody @Valid Authenticate authenticateRequest) {
        return authenticationService.login(authenticateRequest);
    }

    @PostMapping("/verify-token")
    public AppResponse verifyToken(@RequestBody Introspect introspectRequest)
            throws ParseException, JOSEException {
        return authenticationService.introspect(introspectRequest);
    }

    @PostMapping("/logout")
    public AppResponse logout(@RequestBody Logout request) throws ParseException, JOSEException {
        return authenticationService.logout(request);
    }

    @GetMapping("/verify-user")
    public void verifyUser(@RequestParam(name = "code") String verifyCode, HttpServletResponse response)
            throws IOException {
        authenticationService.verifyUser(verifyCode, response);
    }
}
