package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;

@Service
public interface ChapterService {
    public AppResponse createChapter(String slug, CreateChapter createChapterRequest);

    public AppResponse updateChapter(String slug, Integer chapterNumber, UpdateChapter updateChapterRequest);

    public AppResponse deleteChapter(String slug, Integer chapterNumber);

    public AppResponse getChapterByNumber(String slug, Integer chapterNumber);
}
