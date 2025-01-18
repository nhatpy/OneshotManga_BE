package com.anime_social.controller;

import com.anime_social.dto.request.RequestCreateUserDto;
import com.anime_social.dto.request.RequestLoginUserDto;
import com.anime_social.entity.User;
import com.anime_social.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/register")
    public User register(@RequestBody RequestCreateUserDto requestCreateUserDto) {
        return authenticationService.register(requestCreateUserDto);
    }

    @PostMapping("/login")
    public User login(@RequestBody RequestLoginUserDto requestLoginUserDto) {
        return authenticationService.login(requestLoginUserDto);
    }
}
