package com.anime_social.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;
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
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Manga not found")
                    .build();
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

    public AppResponse updateChapter(String id, UpdateChapter updateChapterRequest) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        if (chapter == null) {
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chapter not found")
                    .build();
        }
        updateChapterRequest.getChapterNumber().ifPresent(chapter::setChapterNumber);
        updateChapterRequest.getContent().ifPresent(chapter::setContent);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .data(chapterRepository.save(chapter))
                .build();
    }

    public AppResponse deleteChapter(String id) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        if (chapter == null) {
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chapter not found")
                    .build();
        }

        chapterRepository.delete(chapter);
        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Chapter deleted")
                .build();
    }

    public AppResponse getChapterByNumber(Integer chapterNumber) {
        Chapter chapter = chapterRepository.findByChapterNumber(chapterNumber);
        if (chapter == null) {
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Chapter not found")
                    .build();
        }

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .data(chapter)
                .build();
    }
}
