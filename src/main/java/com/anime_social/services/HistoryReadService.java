package com.anime_social.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.HistoryReadRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Manga;
import com.anime_social.models.User;
import com.anime_social.models.UserReadManga;
import com.anime_social.repositorys.MangaRepository;
import com.anime_social.repositorys.UserReadMangaRepository;
import com.anime_social.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryReadService {
    private final UserReadMangaRepository userReadMangaRepository;
    private final UserRepository userRepository;
    private final MangaRepository mangaRepository;

    public AppResponse checkRead(String userId, String mangaId) {
        Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

        if (userReadManga.isPresent()) {
            return AppResponse.builder()
                    .message("Đã tồn tại lịch sử")
                    .data(true)
                    .build();
        }
        return AppResponse.builder()
                .message("Chưa tồn tại lịch sử")
                .data(false)
                .build();
    }

    public AppResponse read(String userId, String mangaId, HistoryReadRequest historyReadRequest) {
        Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

        if (userReadManga.isPresent()) {
            throw new CusRunTimeException(ErrorCode.HISTORY_EXITS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
        Manga manga = mangaRepository.findById(mangaId)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

        UserReadManga saveUserReadManga = UserReadManga.builder()
                .user(user)
                .manga(manga)
                .lastReadAtChapter(historyReadRequest.getReadChapter())
                .lastReadAtDate(historyReadRequest.getReadDate())
                .build();

        userReadMangaRepository.save(saveUserReadManga);

        return AppResponse.builder()
                .status(HttpStatus.CREATED)
                .build();
    }

    public AppResponse reread(String userId, String mangaId, HistoryReadRequest historyReadRequest) {
        Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

        if (userReadManga.isEmpty()) {
            throw new CusRunTimeException(ErrorCode.HISTORY_NOT_FOUND);
        }

        userReadManga.get().setLastReadAtChapter(historyReadRequest.getReadChapter());
        userReadManga.get().setLastReadAtDate(historyReadRequest.getReadDate());

        userReadMangaRepository.save(userReadManga.get());

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .build();
    }

    public AppResponse unread(String userId, String mangaId) {
        Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

        if (userReadManga.isEmpty()) {
            throw new CusRunTimeException(ErrorCode.HISTORY_NOT_FOUND);
        }

        userReadMangaRepository.delete(userReadManga.get());

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .build();
    }
}
