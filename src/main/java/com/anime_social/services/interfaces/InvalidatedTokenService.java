package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface InvalidatedTokenService {
    public void addToBlacklistedToken(String token);

    public boolean isBlacklistedToken(String token);
}
