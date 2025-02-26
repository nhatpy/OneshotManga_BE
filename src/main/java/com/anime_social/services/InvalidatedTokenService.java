package com.anime_social.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvalidatedTokenService {
    private final StringRedisTemplate stringRedisTemplate;

    public void addToBlacklistedToken(String token) {
        stringRedisTemplate.opsForValue().set(token, token, 10, TimeUnit.MINUTES);
        ;
    }

    public boolean isBlacklistedToken(String token) {
        return stringRedisTemplate.hasKey(token);
    }
}
