package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;

@Service
public interface NotificationService {
    public void createChapterNotification(String mangaId, String mangaName);

    public void paymentSuccessNotification(String userId, String paymentId, long amount);

    public AppResponse getNotifications(String userId, int page, int size);

    public void deleteNotification(String notificationId);
}
