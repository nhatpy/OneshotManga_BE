package com.anime_social.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.PostManga;
import com.anime_social.dto.request.UpdateManga;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.MangaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/manga")
@RequiredArgsConstructor
public class MangaController {
    private final MangaService mangaService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("create")
    public AppResponse createManga(@RequestBody @Valid PostManga request) {
        return mangaService.createManga(request);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("update/{id}")
    public AppResponse updateManga(@PathVariable String id, @RequestBody UpdateManga request) {
        return mangaService.updateManga(id, request);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public AppResponse deleteManga(@PathVariable String id) {
        return mangaService.deleteManga(id);
    }

    @GetMapping("get/{slug}")
    public AppResponse getMangaBySlug(@PathVariable String slug) {
        return mangaService.getMangaBySlug(slug);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("bulk-active")
    public AppResponse bulkActiveManga(@RequestBody List<String> mangaIds) {
        return mangaService.bulkActiveManga(mangaIds);
    }
}
