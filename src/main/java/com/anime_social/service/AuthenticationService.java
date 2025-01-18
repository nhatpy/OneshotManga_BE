package com.anime_social.service;

import com.anime_social.dto.request.RequestCreateUserDto;
import com.anime_social.dto.request.RequestLoginUserDto;
import com.anime_social.entity.User;
import com.anime_social.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    UserRepository userRepository;

    public User register(RequestCreateUserDto requestCreateUserDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        User user = User.builder()
                .email(requestCreateUserDto.getEmail())
                .password(passwordEncoder.encode(requestCreateUserDto.getPassword()))
                .fullName(requestCreateUserDto.getFullName())
                .phoneNumber(requestCreateUserDto.getPhoneNumber())
                .build();
        return userRepository.save(user);
    }

    public User login(RequestLoginUserDto requestLoginUserDto) {
        User user = userRepository.findByEmail(requestLoginUserDto.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(requestLoginUserDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        return user;
    }
}
