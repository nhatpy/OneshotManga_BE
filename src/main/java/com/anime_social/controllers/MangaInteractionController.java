package com.anime_social.controllers;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.services.interfaces.MangaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/interaction")
@RequiredArgsConstructor
public class MangaInteractionController {
    private final MangaService mangaService;

    @PatchMapping("/{mangaId}")
    public void updateMangaInteraction(@PathVariable String mangaId) {
        mangaService.updateMangaInteraction(mangaId);
    }
}
