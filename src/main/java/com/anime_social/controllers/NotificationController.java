package com.anime_social.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String message) {
        simpMessagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void sendNotificationToUser(String userId, String message) {
        String destination = "/queue/notifications/" + userId;
        simpMessagingTemplate.convertAndSend(destination, message);
    }

}