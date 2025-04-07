package com.anime_social.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.HistoryReadRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.HistoryListMangaResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Manga;
import com.anime_social.models.User;
import com.anime_social.models.UserReadManga;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.repositories.UserReadMangaRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.HistoryReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryReadServiceImpl implements HistoryReadService {
        private final UserReadMangaRepository userReadMangaRepository;
        private final UserRepository userRepository;
        private final MangaRepository mangaRepository;

        @Override
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

        @Override
        public AppResponse read(String userId, String mangaId, HistoryReadRequest historyReadRequest) {
                Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

                if (userReadManga.isPresent()) {
                        throw new CusRunTimeException(ErrorCode.HISTORY_EXITS);
                }

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                Manga manga = mangaRepository.findById(mangaId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                manga.setView(manga.getView() + 1);
                Manga savedManga = mangaRepository.save(manga);

                UserReadManga saveUserReadManga = UserReadManga.builder()
                                .user(user)
                                .manga(savedManga)
                                .lastReadAtChapter(historyReadRequest.getReadChapter())
                                .lastReadAtDate(historyReadRequest.getReadDate())
                                .build();

                UserReadManga history = userReadMangaRepository.save(saveUserReadManga);

                return AppResponse.builder()
                                .status(HttpStatus.CREATED)
                                .data(HistoryListMangaResponse.toHistoryListResponse(history))
                                .message("Đã thêm vào lịch sử theo dõi")
                                .build();
        }

        @Override
        public AppResponse reread(String userId, String mangaId, HistoryReadRequest historyReadRequest) {
                Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);
                Manga manga = mangaRepository.findById(mangaId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                manga.setView(manga.getView() + 1);
                mangaRepository.save(manga);

                if (userReadManga.isEmpty()) {
                        throw new CusRunTimeException(ErrorCode.HISTORY_NOT_FOUND);
                }

                userReadManga.get().setLastReadAtChapter(historyReadRequest.getReadChapter());
                userReadManga.get().setLastReadAtDate(historyReadRequest.getReadDate());

                UserReadManga savedHistory = userReadMangaRepository.save(userReadManga.get());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã cập nhật lịch sử theo dõi")
                                .data(HistoryListMangaResponse.toHistoryListResponse(savedHistory))
                                .build();
        }

        @Override
        public AppResponse unread(String userId, String mangaId) {
                Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

                if (userReadManga.isEmpty()) {
                        throw new CusRunTimeException(ErrorCode.HISTORY_NOT_FOUND);
                }

                userReadMangaRepository.delete(userReadManga.get());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã xóa truyện khỏi lịch sử theo dõi")
                                .build();
        }

        @Override
        public AppResponse getHistoryListMangaPaging(String userId, int page, int size) {
                int staterPage = page - 1;
                Pageable pageable = PageRequest.of(staterPage, size);

                Integer total = userReadMangaRepository.countByUserIdAndMangaId(userId).orElse(0);
                List<UserReadManga> userReadMangas = userReadMangaRepository.findAllByUserIdPaging(userId, pageable);

                List<HistoryListMangaResponse> historyListMangaResponses = userReadMangas.stream()
                                .map(HistoryListMangaResponse::toHistoryListResponse)
                                .collect(Collectors.toList());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(historyListMangaResponses)
                                .totalItem(total)
                                .build();
        }
}
