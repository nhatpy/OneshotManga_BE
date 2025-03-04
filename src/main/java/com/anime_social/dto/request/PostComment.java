package com.anime_social.dto.request;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostComment {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String userId;
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String chapterId;
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String content;
}
