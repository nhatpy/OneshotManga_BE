package com.anime_social.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.ChatbotRequest;
import com.anime_social.services.interfaces.ChatbotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @GetMapping("/get-history/{userId}")
    public List<String> getHitory(@PathVariable String userId) {
        return chatbotService.getHistory(userId);
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestBody ChatbotRequest chatbotRequest) {
        return chatbotService.sendMessage(chatbotRequest);
    }
}
