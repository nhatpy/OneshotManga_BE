package com.anime_social.controller;

import com.anime_social.dto.request.RequestUserDto;
import com.anime_social.entity.User;
import com.anime_social.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RequestUserDto requestUserDto) {
        return userService.register(requestUserDto);
    }

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
