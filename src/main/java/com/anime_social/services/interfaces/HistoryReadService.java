package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.HistoryReadRequest;
import com.anime_social.dto.response.AppResponse;

@Service
public interface HistoryReadService {
    public AppResponse read(String userId, String mangaId, HistoryReadRequest historyReadRequest);

    public AppResponse unread(String userId, String mangaId);

    public AppResponse getHistoryListMangaPaging(String userId, int page, int size);
}
