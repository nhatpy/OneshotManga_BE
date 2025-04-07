package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;

@Service
public interface ChapterService {
    public AppResponse createChapter(String mangaId, CreateChapter createChapterRequest);

    public AppResponse updateChapter(String mangaId, Integer chapterNumber, UpdateChapter updateChapterRequest);

    public AppResponse deleteChapter(String mangaId, Integer chapterNumber);

    public AppResponse getChapterByNumber(String mangaId, Integer chapterNumber);
}
