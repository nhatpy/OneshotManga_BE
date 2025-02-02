package com.anime_social.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends BaseEntity{
    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "wallet")
    @Builder.Default
    Integer wallet = 0;

    @Column(name = "role")
    HashSet<String> role;

    @Column(name = "isVerified")
    @Builder.Default
    Boolean isVerified = false;

    @Column(name = "is_warning")
    @Builder.Default
    Boolean isWarning = false;

    @Column(name = "is_banned")
    @Builder.Default
    Boolean isBanned = false;
}
