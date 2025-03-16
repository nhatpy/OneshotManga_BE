package com.anime_social.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.HistoryReadRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.HistoryReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/history-read/{userId}")
@RequiredArgsConstructor
public class HistoryReadController {
    private final HistoryReadService historyReadService;

    @GetMapping("/check/{mangaId}")
    public AppResponse checkRead(
            @PathVariable String mangaId,
            @PathVariable String userId) {
        return historyReadService.checkRead(userId, mangaId);
    }

    @PostMapping("/read/{mangaId}")
    public AppResponse read(
            @PathVariable String mangaId,
            @PathVariable String userId,
            @RequestBody HistoryReadRequest historyReadRequest) {
        return historyReadService.read(userId, mangaId, historyReadRequest);
    }

    @PatchMapping("/reread/{mangaId}")
    public AppResponse reread(
            @PathVariable String mangaId,
            @PathVariable String userId,
            @RequestBody HistoryReadRequest historyReadRequest) {
        return historyReadService.reread(userId, mangaId, historyReadRequest);
    }

    @DeleteMapping("/unread/{mangaId}")
    public AppResponse unread(
            @PathVariable String mangaId,
            @PathVariable String userId) {
        return historyReadService.unread(userId, mangaId);
    }

    @GetMapping("/get-paging")
    public AppResponse getHistoryListMangaPaging(
            @PathVariable String userId,
            @RequestParam int page,
            @RequestParam int size) {
        return historyReadService.getHistoryListMangaPaging(userId, page, size);
    }
}
