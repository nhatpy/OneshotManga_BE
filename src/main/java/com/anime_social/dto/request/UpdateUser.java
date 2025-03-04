package com.anime_social.dto.request;

import java.util.Optional;

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
public class UpdateUser {
    @Size(min = 6, message = "PASSWORD_AT_LEAST_6_CHARACTERS")
    Optional<String> password;
    Optional<String> avatar;
    Optional<String> fullName;
    Optional<Integer> wallet;
    Optional<Boolean> isWarning;
}
