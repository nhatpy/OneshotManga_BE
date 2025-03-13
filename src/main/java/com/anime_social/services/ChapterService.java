package com.anime_social.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Chapter;
import com.anime_social.models.Manga;
import com.anime_social.repositorys.ChapterRepository;
import com.anime_social.repositorys.MangaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChapterService {
        private final ChapterRepository chapterRepository;
        private final MangaRepository mangaRepository;

        public AppResponse createChapter(CreateChapter createChapterRequest) {
                Manga manga = mangaRepository.findById(createChapterRequest.getMangaId()).orElse(null);
                if (manga == null) {
                        throw new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND);
                }

                Chapter chapter = Chapter.builder()
                                .manga(manga)
                                .content(createChapterRequest.getContent())
                                .chapterNumber(createChapterRequest.getChapterNumber())
                                .build();

                return AppResponse.builder()
                                .status(HttpStatus.CREATED)
                                .data(chapterRepository.save(chapter))
                                .build();
        }

        public AppResponse updateChapter(Integer chapterNumber, UpdateChapter updateChapterRequest) {
                Chapter chapter = chapterRepository.findByChapterNumber(chapterNumber)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                updateChapterRequest.getChapterNumber()
                                .ifPresent(newChapterNumber -> chapter.setChapterNumber(newChapterNumber));
                updateChapterRequest.getContent().ifPresent(content -> chapter.setContent(content));

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(chapterRepository.save(chapter))
                                .build();
        }

        public AppResponse deleteChapter(Integer chapterNumber) {
                Chapter chapter = chapterRepository.findByChapterNumber(chapterNumber)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                chapterRepository.delete(chapter);
                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Xóa chapter thành công")
                                .build();
        }

        public AppResponse getChapterByNumber(Integer chapterNumber) {
                Chapter chapter = chapterRepository.findByChapterNumber(chapterNumber)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(chapter)
                                .build();
        }
}
