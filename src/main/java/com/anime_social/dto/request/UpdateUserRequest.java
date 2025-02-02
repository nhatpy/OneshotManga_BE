package com.anime_social.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @NotEmpty(message = "YOU_MISSING_REQUIRED_FIELD")
    @Size(min = 6, message = "PASSWORD_AT_LEAST_6_CHARACTERS")
    String password;
    String avatar;
    String fullName;
    Integer wallet;
    Boolean isVerified;
    Boolean isWarning;
    Boolean isBanned;
}
