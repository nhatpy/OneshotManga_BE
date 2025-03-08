package com.anime_social.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.AddToFollowListRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.FollowMangaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowMangaController {
    private final FollowMangaService followMangaService;

    @PostMapping("/")
    public AppResponse addToFollowList(@RequestBody @Valid AddToFollowListRequest request) {
        return followMangaService.addToFollowList(request);
    }

    @DeleteMapping("/delete")
    public AppResponse deleteFromFollowList(@RequestBody @Valid AddToFollowListRequest request) {
        return followMangaService.deleteFromFollowList(request);
    }
}
