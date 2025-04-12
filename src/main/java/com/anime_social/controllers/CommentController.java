package com.anime_social.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.PostComment;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.interfaces.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public AppResponse createComment(@Valid @RequestBody PostComment request) {
        return commentService.createComment(request);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public AppResponse deleteComment(@PathVariable String id) {
        return commentService.deleteComment(id);
    }

    @GetMapping("/get/{chapterId}")
    public AppResponse getCommentByChapterId(
            @PathVariable String chapterId,
            @RequestParam int page,
            @RequestParam int size) {
        return commentService.getCommentByChapterId(chapterId, page, size);
    }
}