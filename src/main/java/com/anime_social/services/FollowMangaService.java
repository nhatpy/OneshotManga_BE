package com.anime_social.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;
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

                Optional<FollowMangaList> followMangaList = followMangaListRepository.findById(userId);

                if (!followMangaList.isPresent()) {
                        FollowMangaList newFollowMangaList = FollowMangaList.builder()
                                        .user(user)
                                        .build();

                        FollowMangaList saveList = followMangaListRepository.save(newFollowMangaList);

                        followMangaListMangaRepository.save(FollowMangaListManga.builder()
                                        .followMangaList(saveList)
                                        .manga(manga)
                                        .build());
                }
                followMangaListMangaRepository.save(FollowMangaListManga.builder()
                                .followMangaList(followMangaList.get())
                                .manga(manga)
                                .build());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Thêm vào danh sách theo dõi thành công")
                                .build();
        }

        public AppResponse deleteFromFollowList(String mangaId, String userId) {
                Optional<FollowMangaListManga> followMangaListManga = followMangaListMangaRepository
                                .findByFollowMangaListIdAndMangaId(userId, mangaId);

                if (!followMangaListManga.isPresent()) {
                        throw new CusRunTimeException(ErrorCode.FOLLOW_MANGA_LIST_NOT_FOUND);
                }

                followMangaListMangaRepository.delete(followMangaListManga.get());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Xóa khỏi danh sách theo dõi thành công")
                                .build();
        }

}
