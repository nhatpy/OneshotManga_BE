package com.anime_social.service;

import com.anime_social.dto.request.RequestUserDto;
import com.anime_social.entity.User;
import com.anime_social.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;

    public User register(RequestUserDto requestUserDto) {
        User user = User.builder()
                .email(requestUserDto.getEmail())
                .password(requestUserDto.getPassword())
                .fullName(requestUserDto.getFullName())
                .phoneNumber(requestUserDto.getPhoneNumber())
                .build();
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
