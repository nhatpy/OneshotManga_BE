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
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.anime_social.dto.request.CreateChapter;
import com.anime_social.dto.request.UpdateChapter;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.ChapterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
@RestControllerAdvice
public class ChapterController {
    private final ChapterService chapterService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public AppResponse createChapter(@RequestBody @Valid CreateChapter createChapterRequest) {
        return chapterService.createChapter(createChapterRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/{id}")
    public AppResponse updateChapter(@PathVariable String id,
            @RequestBody UpdateChapter updateChapterRequest) {
        return chapterService.updateChapter(id, updateChapterRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public AppResponse deleteChapter(@PathVariable String id) {
        return chapterService.deleteChapter(id);
    }

    @GetMapping("/get/{chapterNumber}")
    public AppResponse getChapter(@PathVariable Integer chapterNumber) {
        return chapterService.getChapterByNumber(chapterNumber);
    }
}
