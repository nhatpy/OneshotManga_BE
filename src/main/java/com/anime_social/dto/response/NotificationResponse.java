package com.anime_social.dto.response;

import java.util.Optional;

import com.anime_social.models.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NotificationResponse {
    String id;
    String content;
    String type;
    String userId;

    public static NotificationResponse toNotificationResponse(Notification notification) {
        String userId = Optional.ofNullable(notification.getUser()).map(user -> user.getId()).orElse(null);

        return NotificationResponse.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .type(notification.getType().name())
                .userId(userId)
                .build();
    }
}
