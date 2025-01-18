package com.anime_social.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateUserDto {
    @NotEmpty(message = "Email is required")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email is invalid")
    String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotEmpty(message = "Full name is required")
    String fullName;

    @NotEmpty(message = "Phone number is required")
    String phoneNumber;
}
