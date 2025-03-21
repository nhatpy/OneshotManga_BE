package com.anime_social.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationRestController {
    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public AppResponse getNotificationsByUser(
            @PathVariable String userId,
            @RequestParam int page,
            @RequestParam int size) {
        return notificationService.getNotifications(userId, page, size);
    }
}
