package com.anime_social.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.anime_social.services.interfaces.InvalidatedTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addToBlacklistedToken(String token) {
        redisTemplate.opsForValue().set(token, token, 10, TimeUnit.MINUTES);
    }

    @Override
    public boolean isBlacklistedToken(String token) {
        return redisTemplate.hasKey(token);
    }
}
