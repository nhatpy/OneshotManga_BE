package com.anime_social.services.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.ChatbotRequest;

@Service
public interface ChatbotService {

    List<String> getHistory(String userId);

    String sendMessage(ChatbotRequest chatbotRequest);

}
