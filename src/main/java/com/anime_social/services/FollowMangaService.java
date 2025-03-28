package com.anime_social.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.FollowListMangaResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.FollowMangaList;
import com.anime_social.models.FollowMangaListManga;
import com.anime_social.models.Manga;
import com.anime_social.models.User;
import com.anime_social.repositorys.FollowMangaListMangaRepository;
import com.anime_social.repositorys.FollowMangaListRepository;
import com.anime_social.repositorys.MangaRepository;
import com.anime_social.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowMangaService {
        private final FollowMangaListMangaRepository followMangaListMangaRepository;
        private final FollowMangaListRepository followMangaListRepository;
        private final MangaRepository mangaRepository;
        private final UserRepository userRepository;

        public AppResponse addToFollowList(String mangaId, String userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                Manga manga = mangaRepository.findById(mangaId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                manga.setFollow(manga.getFollow() + 1);

                Optional<FollowMangaList> followMangaList = followMangaListRepository.findById(userId);

                if (!followMangaList.isPresent()) {
                        FollowMangaList newFollowMangaList = FollowMangaList.builder()
                                        .user(user)
                                        .build();

                        FollowMangaList savedList = followMangaListRepository.save(newFollowMangaList);

                        FollowMangaListManga savedFollowMangaListManga = followMangaListMangaRepository
                                        .save(FollowMangaListManga.builder()
                                                        .followMangaList(savedList)
                                                        .manga(mangaRepository.save(manga))
                                                        .build());

                        return AppResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("Thêm vào danh sách theo dõi thành công")
                                        .data(FollowListMangaResponse.toFollowListResponse(savedFollowMangaListManga))
                                        .build();
                }
                FollowMangaListManga savedFollowMangaListManga = followMangaListMangaRepository
                                .save(FollowMangaListManga.builder()
                                                .followMangaList(followMangaList.get())
                                                .manga(mangaRepository.save(manga))
                                                .build());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Thêm vào danh sách theo dõi thành công")
                                .data(FollowListMangaResponse.toFollowListResponse(savedFollowMangaListManga))
                                .build();
        }

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
                                .message("Xóa khỏi danh sách theo dõi thành công")
                                .build();
        }

        public AppResponse getFollowListPaging(String userId, int page, int size) {
                int starterPage = page - 1;
                Pageable pageable = PageRequest.of(starterPage, size);

                List<FollowMangaListManga> followMangaListMangas = followMangaListMangaRepository
                                .findMangainListByFollowMangaListIdPaging(userId, pageable);

                List<FollowListMangaResponse> followListMangaResponses = followMangaListMangas.stream()
                                .map(FollowListMangaResponse::toFollowListResponse)
                                .collect(Collectors.toList());

                Integer total = followMangaListMangaRepository.countMangaInListByFollowListId(userId)
                                .orElse(0);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(followListMangaResponses)
                                .totalItem(total)
                                .message("Lấy danh sách theo dõi thành công")
                                .build();
        }

}
