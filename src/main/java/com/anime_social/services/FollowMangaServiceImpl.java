package com.anime_social.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.MangaResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.FollowMangaList;
import com.anime_social.models.FollowMangaListManga;
import com.anime_social.models.Manga;
import com.anime_social.models.MangaInteraction;
import com.anime_social.models.User;
import com.anime_social.repositories.FollowMangaListMangaRepository;
import com.anime_social.repositories.FollowMangaListRepository;
import com.anime_social.repositories.MangaInteractionRepository;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.FollowMangaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowMangaServiceImpl implements FollowMangaService {
        private final FollowMangaListMangaRepository followMangaListMangaRepository;
        private final FollowMangaListRepository followMangaListRepository;
        private final MangaRepository mangaRepository;
        private final UserRepository userRepository;
        private final MangaInteractionRepository mangaInteractionRepository;

        @Override
        public AppResponse addToFollowList(String mangaId, String userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                Manga manga = mangaRepository.findById(mangaId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                MangaInteraction mangaInteraction = mangaInteractionRepository.findById(mangaId)
                                .orElse(null);

                if (mangaInteraction == null) {
                        MangaInteraction newMangaInteraction = MangaInteraction.builder()
                                        .manga(manga)
                                        .build();
                        mangaInteractionRepository.save(newMangaInteraction);
                } else {
                        mangaInteraction.setTime(mangaInteraction.getTime() + 1);
                        mangaInteractionRepository.save(mangaInteraction);
                }

                manga.setFollow(manga.getFollow() + 1);

                Optional<FollowMangaList> followMangaList = followMangaListRepository.findById(userId);

                if (!followMangaList.isPresent()) {
                        FollowMangaList newFollowMangaList = FollowMangaList.builder()
                                        .user(user)
                                        .build();

                        FollowMangaList savedList = followMangaListRepository.save(newFollowMangaList);

                        followMangaListMangaRepository.save(FollowMangaListManga.builder()
                                        .followMangaList(savedList)
                                        .manga(mangaRepository.save(manga))
                                        .build());

                        return AppResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("Thêm vào danh sách theo dõi thành công")
                                        .data(MangaResponse.toMangaResponse(manga))
                                        .build();
                }
                followMangaListMangaRepository.save(FollowMangaListManga.builder()
                                .followMangaList(followMangaList.get())
                                .manga(mangaRepository.save(manga))
                                .build());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Thêm vào danh sách theo dõi thành công")
                                .data(MangaResponse.toMangaResponse(manga))
                                .build();
        }

        @Override
        public AppResponse deleteFromFollowList(String mangaId, String userId) {
                Manga manga = mangaRepository.findById(mangaId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));
                manga.setFollow(manga.getFollow() - 1);
                mangaRepository.save(manga);

                Optional<FollowMangaListManga> followMangaListManga = followMangaListMangaRepository
                                .findByFollowMangaListIdAndMangaId(userId, mangaId);

                if (followMangaListManga.isEmpty()) {
                        throw new CusRunTimeException(ErrorCode.FOLLOW_MANGA_LIST_NOT_FOUND);
                }

                followMangaListMangaRepository.delete(followMangaListManga.get());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã xóa khỏi danh sách theo dõi")
                                .build();
        }

        @Override
        public AppResponse getFollowListPaging(String userId, int page, int size) {
                int starterPage = page - 1;
                Pageable pageable = PageRequest.of(starterPage, size);

                List<FollowMangaListManga> followMangaListMangas = followMangaListMangaRepository
                                .findMangainListByFollowMangaListIdPaging(userId, pageable);

                List<MangaResponse> mangaResponses = followMangaListMangas.stream()
                                .map((followMangaListManga) -> MangaResponse
                                                .toMangaResponse(followMangaListManga.getManga()))
                                .collect(Collectors.toList());

                Integer total = followMangaListMangaRepository.countMangaInListByFollowListId(userId)
                                .orElse(0);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(mangaResponses)
                                .totalItem(total)
                                .message("Lấy danh sách theo dõi thành công")
                                .build();
        }

        @Override
        public boolean checkFollowManga(String mangaId, String userId) {
                Optional<FollowMangaListManga> followMangaListManga = followMangaListMangaRepository
                                .findByFollowMangaListIdAndMangaId(userId, mangaId);

                if (followMangaListManga.isPresent()) {
                        return true;
                }
                return false;
        }

}
