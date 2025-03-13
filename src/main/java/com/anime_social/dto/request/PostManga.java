package com.anime_social.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostManga {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String authorId;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    List<String> categoryIds;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String name;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String slug;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String description;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String coverImg;
    Boolean isDone;
}
