package com.anime_social.dto.request;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChatbotRequest {
    String message;
    String userId;

    public Optional<String> getUserId() {
        try {
            return Optional.ofNullable(userId).map(String::valueOf);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
