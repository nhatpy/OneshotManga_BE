package com.anime_social.configurations;

import com.anime_social.enums.Role;
import com.anime_social.models.User;
import com.anime_social.repositorys.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            HashSet<String> roles = new HashSet<String>();
            roles.add(Role.ADMIN.name());

            User user = User.builder()
                    .fullName("admin")
                    .email("admin@gmail.com")
                    .role(roles)
                    .isVerified(true)
                    .password(passwordEncoder.encode("000000"))
                    .build();

            if (userRepository.findByFullName("admin") != null) {
                log.warn("admin already exists with password is 000000");
                return;
            }
            userRepository.save(user);
            log.info("admin has been created with password is 000000");
        };
    }
}
