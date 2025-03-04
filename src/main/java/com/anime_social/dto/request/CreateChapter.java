package com.anime_social.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateChapter {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String mangaId;
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    Integer chapterNumber;
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    List<String> content;
}
