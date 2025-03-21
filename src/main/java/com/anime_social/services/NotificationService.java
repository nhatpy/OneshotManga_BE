package com.anime_social.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.controllers.NotificationController;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.NotificationResponse;
import com.anime_social.enums.NotiType;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Notification;
import com.anime_social.models.User;
import com.anime_social.repositorys.FollowMangaListMangaRepository;
import com.anime_social.repositorys.NotificationRepository;
import com.anime_social.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
        private final FollowMangaListMangaRepository followMangaListMangaRepository;
        private final NotificationController notificationController;
        private final UserRepository userRepository;
        private final NotificationRepository notificationRepository;

        public void createChapterNotification(String mangaId, String mangaName) {
                List<String> userIds = followMangaListMangaRepository.findFollowMangaListIdByMangaId(mangaId);
                List<User> users = userRepository.findAllById(userIds);

                users.forEach(user -> {
                        Notification notification = Notification.builder()
                                        .content("Có chapter mới của truyện " + mangaName)
                                        .type(NotiType.PRIVATE)
                                        .user(user)
                                        .build();
                        notificationRepository.save(notification);
                        notificationController.sendNotificationToUser(user.getId(),
                                        "Có chapter mới của truyện " + mangaName);
                });
        }

        public void paymentSuccessNotification(String userId, String paymentId, long amount) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                Notification notification = Notification.builder()
                                .content("Bạn đã nạp thành công " + amount + " VNĐ")
                                .type(NotiType.PRIVATE)
                                .user(user)
                                .build();
                notificationRepository.save(notification);
                notificationController.sendNotificationToUser(userId,
                                "Hóa đơn " + paymentId + " đã thanh toán thành công " + amount + " VNĐ");
        }

        public AppResponse getNotifications(String userId, int page, int size) {
                int starterPage = page - 1;
                Pageable pageable = PageRequest.of(starterPage, size, Sort.by("type").ascending());
                List<Notification> notifications = notificationRepository.findByUserIdAndSort(userId, pageable);

                List<NotificationResponse> notificationResponses = notifications.stream()
                                .map(notification -> NotificationResponse.toNotificationResponse(notification))
                                .collect(Collectors.toList());

                Integer total = notificationRepository.countByUserId(userId).orElse(0);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Lấy thông báo thành công")
                                .totalItem(total)
                                .data(notificationResponses)
                                .build();
        }
}
