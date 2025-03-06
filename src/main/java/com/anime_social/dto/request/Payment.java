package com.anime_social.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String userId;
    @NotNull(message = "YOU_MISSING_REQUIRED_FIELD")
    long amount;
}
