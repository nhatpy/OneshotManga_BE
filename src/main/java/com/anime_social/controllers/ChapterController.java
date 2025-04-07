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

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.interfaces.ChapterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chapter/{mangaId}")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public AppResponse createChapter(
            @PathVariable String mangaId,
            @RequestBody @Valid CreateChapter createChapterRequest) {
        return chapterService.createChapter(mangaId, createChapterRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/{chapterNumber}")
    public AppResponse updateChapter(
            @PathVariable String mangaId,
            @PathVariable Integer chapterNumber,
            @RequestBody UpdateChapter updateChapterRequest) {
        return chapterService.updateChapter(mangaId, chapterNumber, updateChapterRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{chapterNumber}")
    public AppResponse deleteChapter(
            @PathVariable String mangaId,
            @PathVariable Integer chapterNumber) {
        return chapterService.deleteChapter(mangaId, chapterNumber);
    }

    @GetMapping("/get/{chapterNumber}")
    public AppResponse getChapter(
            @PathVariable String mangaId,
            @PathVariable Integer chapterNumber) {
        return chapterService.getChapterByNumber(mangaId, chapterNumber);
    }
}
