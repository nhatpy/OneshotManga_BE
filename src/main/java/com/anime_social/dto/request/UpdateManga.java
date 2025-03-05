package com.anime_social.dto.request;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateManga {
    Optional<String> name;
    Optional<String> slug;
    Optional<String> description;
    Optional<String> coverImg;
    Optional<Boolean> isDone;
}
