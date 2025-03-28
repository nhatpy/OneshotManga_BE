package com.anime_social.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import com.anime_social.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse implements Serializable {
    String id;
    String email;
    String password;
    String fullName;
    String avatar;
    Integer wallet;
    HashSet<String> role;
    Boolean isWarning;
    Boolean isBanned;
    Date createAt;
    Date updateAt;

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .wallet(user.getWallet())
                .role(user.getRole())
                .isWarning(user.getIsWarning())
                .isBanned(user.getIsBanned())
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .build();
    }
}
