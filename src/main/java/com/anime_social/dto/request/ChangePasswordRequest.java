package com.anime_social.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 6, message = "PASSWORD_AT_LEAST_6_CHARACTERS")
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    String newPassword;
}
