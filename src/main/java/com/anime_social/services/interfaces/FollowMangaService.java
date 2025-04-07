package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;

@Service
public interface FollowMangaService {
    public AppResponse addToFollowList(String mangaId, String userId);

    public AppResponse deleteFromFollowList(String mangaId, String userId);

    public AppResponse getFollowListPaging(String userId, int page, int size);

}
