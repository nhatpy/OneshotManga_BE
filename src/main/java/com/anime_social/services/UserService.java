package com.anime_social.services;

import com.anime_social.dto.request.UpdateUser;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.models.User;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.repositorys.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;

    public AppResponse getUsers() {
        List<User> users = userRepository.findAll();

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách người dùng thành công")
                .data(users)
                .build();
    }

    public AppResponse getUserById(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new CusRunTimeException(ErrorCode.USER_NOT_FOUND);
        } else {
            return AppResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Lấy thông tin người dùng thành công")
                    .data(user.get())
                    .build();
        }
    }

    public AppResponse updateUser(String id, UpdateUser userRequest) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new CusRunTimeException(ErrorCode.USER_NOT_FOUND);
        } else {
            User userUpdate = user.get();

            userRequest.getFullName().ifPresent(userUpdate::setFullName);
            userRequest.getAvatar().ifPresent(userUpdate::setAvatar);
            userRequest.getWallet().ifPresent(userUpdate::setWallet);
            userRequest.getIsWarning().ifPresent(userUpdate::setIsWarning);

            userRepository.save(userUpdate);

            return AppResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Cập nhật thông tin người dùng thành công")
                    .data(userUpdate)
                    .build();
        }
    }

    public AppResponse deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new CusRunTimeException(ErrorCode.USER_NOT_FOUND);
        } else {
            userRepository.deleteById(id);

            return AppResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Xóa người dùng thành công")
                    .build();
        }
    }

    public AppResponse currentUser() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email);

        if (currentUser == null) {
            throw new CusRunTimeException(ErrorCode.EMAIL_NOT_FOUND);
        }

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin người dùng hiện tại thành công")
                .data(currentUser)
                .build();
    }

    public AppResponse warningUser(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new CusRunTimeException(ErrorCode.USER_NOT_FOUND);
        } else {
            User userUpdate = user.get();
            if (userUpdate.getIsWarning()) {
                userUpdate.setIsBanned(true);

                userRepository.save(userUpdate);

                return AppResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Đã khoá tài khoản người dùng")
                        .data(userUpdate)
                        .build();
            }
            userUpdate.setIsWarning(true);

            userRepository.save(userUpdate);

            return AppResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Cảnh báo người dùng thành công")
                    .data(userUpdate)
                    .build();
        }
    }
}
