package com.anime_social.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvalidatedTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public void addToBlacklistedToken(String token) {
        redisTemplate.opsForValue().set(token, token, 10, TimeUnit.MINUTES);
    }

    public boolean isBlacklistedToken(String token) {
        return redisTemplate.hasKey(token);
    }
}
