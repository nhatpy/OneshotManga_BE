package com.anime_social.services.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface OllamaService {
    public List<String> extractGenres(String message);
}
