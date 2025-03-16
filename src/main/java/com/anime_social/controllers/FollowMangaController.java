package com.anime_social.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.FollowMangaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow/{userId}")
public class FollowMangaController {
    private final FollowMangaService followMangaService;

    @PostMapping("/{mangaId}")
    public AppResponse addToFollowList(
            @PathVariable String mangaId,
            @PathVariable String userId) {
        return followMangaService.addToFollowList(mangaId, userId);
    }

    @DeleteMapping("/delete/{mangaId}")
    public AppResponse deleteFromFollowList(
            @PathVariable String mangaId,
            @PathVariable String userId) {
        return followMangaService.deleteFromFollowList(mangaId, userId);
    }

    @GetMapping("/get-paging")
    public AppResponse getFollowListPaging(
            @PathVariable String userId,
            @RequestParam int page,
            @RequestParam int size) {
        return followMangaService.getFollowListPaging(userId, page, size);
    }
}
