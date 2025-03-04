package com.anime_social.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateCategory {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String name;

    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String description;
}
