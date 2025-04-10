package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.BulkActiveRequest;
import com.anime_social.dto.request.PostManga;
import com.anime_social.dto.request.UpdateManga;
import com.anime_social.dto.response.AppResponse;

@Service
public interface MangaService {
    public AppResponse createManga(PostManga request);

    public AppResponse updateManga(String slug, UpdateManga request);

    public AppResponse deleteManga(String slug);

    public AppResponse getMangaBySlug(String slug);

    public AppResponse bulkActiveManga(BulkActiveRequest bulkActiveRequest);

    public AppResponse getMangaPaging(int page, int size, int type);

    public AppResponse getByAuthorId(int page, int size, String authorId);
}
