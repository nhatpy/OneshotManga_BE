package com.anime_social.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.ChapterResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Chapter;
import com.anime_social.models.Manga;
import com.anime_social.repositories.ChapterRepository;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.services.interfaces.ChapterService;
import com.anime_social.services.interfaces.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {
        private final ChapterRepository chapterRepository;
        private final MangaRepository mangaRepository;
        private final NotificationService notificationService;

        @Override
        public AppResponse createChapter(String slug, CreateChapter createChapterRequest) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                Chapter chapterExist = chapterRepository
                                .findByChapterNumberAndMangaSlug(
                                                slug, createChapterRequest.getChapterNumber())
                                .orElse(null);
                if (chapterExist != null) {
                        throw new CusRunTimeException(ErrorCode.CHAPTER_ALREADY_EXISTS);
                }

                Chapter chapter = Chapter.builder()
                                .manga(manga)
                                .content(createChapterRequest.getContent())
                                .chapterNumber(createChapterRequest.getChapterNumber())
                                .build();
                Chapter savedChapter = chapterRepository.save(chapter);

                notificationService.createChapterNotification(manga.getId(), manga.getName());

                Sort sort = Sort.by(Sort.Direction.DESC, "chapterNumber");
                List<Chapter> chapters = chapterRepository.findByMangaSlug(slug, sort);

                List<Integer> numberOfChapter = chapters.stream()
                                .map(Chapter::getChapterNumber)
                                .collect(Collectors.toList());

                return AppResponse.builder()
                                .status(HttpStatus.CREATED)
                                .data(ChapterResponse.toChapterResponse(savedChapter, numberOfChapter))
                                .message("Tạo chapter thành công")
                                .build();
        }

        @Override
        public AppResponse updateChapter(String slug, Integer chapterNumber, UpdateChapter updateChapterRequest) {
                Chapter chapter = chapterRepository.findByChapterNumberAndMangaSlug(
                                slug, chapterNumber)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                updateChapterRequest.getContent().ifPresent(content -> chapter.setContent(content));
                Chapter savedChapter = chapterRepository.save(chapter);

                Sort sort = Sort.by(Sort.Direction.DESC, "chapterNumber");
                List<Chapter> chapters = chapterRepository.findByMangaSlug(slug, sort);

                List<Integer> numberOfChapter = chapters.stream()
                                .map(Chapter::getChapterNumber)
                                .collect(Collectors.toList());
                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(ChapterResponse.toChapterResponse(savedChapter, numberOfChapter))
                                .message("Cập nhật chapter thành công")
                                .build();
        }

        @Override
        public AppResponse deleteChapter(String slug, Integer chapterNumber) {
                Chapter chapter = chapterRepository.findByChapterNumberAndMangaSlug(
                                slug, chapterNumber)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                chapterRepository.delete(chapter);
                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Xóa chapter thành công")
                                .build();
        }

        @Override
        public AppResponse getChapterByNumber(String slug, Integer chapterNumber) {
                Chapter chapter = chapterRepository.findByChapterNumberAndMangaSlug(
                                slug, chapterNumber)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                Sort sort = Sort.by(Sort.Direction.DESC, "chapterNumber");
                List<Chapter> chapters = chapterRepository.findByMangaSlug(slug, sort);

                List<Integer> numberOfChapter = chapters.stream()
                                .map(Chapter::getChapterNumber)
                                .collect(Collectors.toList());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(ChapterResponse.toChapterResponse(chapter, numberOfChapter))
                                .message("Lấy chapter theo số tập thành công")
                                .build();
        }
}
