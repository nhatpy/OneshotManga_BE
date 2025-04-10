package com.anime_social.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.BulkActiveRequest;
import com.anime_social.dto.request.PostManga;
import com.anime_social.dto.request.UpdateManga;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.interfaces.MangaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/manga")
@RequiredArgsConstructor
public class MangaController {
    private final MangaService mangaService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public AppResponse createManga(@RequestBody @Valid PostManga request) {
        return mangaService.createManga(request);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/{slug}")
    public AppResponse updateManga(@PathVariable String slug, @RequestBody UpdateManga request) {
        return mangaService.updateManga(slug, request);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{slug}")
    public AppResponse deleteManga(@PathVariable String slug) {
        return mangaService.deleteManga(slug);
    }

    @GetMapping("/get/{slug}")
    public AppResponse getMangaBySlug(@PathVariable String slug) {
        return mangaService.getMangaBySlug(slug);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bulk-active")
    public AppResponse bulkActiveManga(@RequestBody BulkActiveRequest bulkActiveRequest) {
        return mangaService.bulkActiveManga(bulkActiveRequest);
    }

    @GetMapping("/get/get-paging")
    public AppResponse getMangaPaging(
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size,
            @RequestParam(required = true) int type) {
        return mangaService.getMangaPaging(page, size, type);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/get-by-author/{authorId}")
    public AppResponse getByAuthorId(
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size,
            @PathVariable(required = true) String authorId) {
        return mangaService.getByAuthorId(page, size, authorId);
    }
}
