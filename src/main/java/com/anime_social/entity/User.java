package com.anime_social.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @NotEmpty(message = "Email is required")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email is invalid")
    @Column(name = "email", unique = true, nullable = false)
    String email;

    @NotEmpty(message = "Password is required")
    @Column(name = "password", nullable = false)
    String password;

    @NotEmpty(message = "Full name is required")
    @Column(name = "full_name", unique = true, nullable = false)
    String fullName;

    @NotEmpty(message = "Phone number is required")
    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "wallet")
    @Builder.Default
    Integer wallet = 0;

    @Column(name = "role")
    String role;

    @Column(name = "isVerified")
    @Builder.Default
    Boolean isVerified = false;

    @Column(name = "is_warning")
    @Builder.Default
    Boolean isWarning = false;

    @Column(name = "is_banned")
    @Builder.Default
    Boolean isBanned = false;

    @Column(name = "create_at")
    @Builder.Default
    Date createAt = new Date();

    @Column(name = "update_at")
    @Builder.Default
    Date updateAt = new Date();
}
