package com.anime_social.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.PostComment;
import com.anime_social.dto.request.UpdateComment;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.models.Chapter;
import com.anime_social.models.Comment;
import com.anime_social.models.User;
import com.anime_social.repositorys.ChapterRepository;
import com.anime_social.repositorys.CommentRepository;
import com.anime_social.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;

    public AppResponse createComment(PostComment request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .chapter(chapter)
                .build();

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Comment created successfully")
                .data(commentRepository.save(comment))
                .build();
    }

    public AppResponse deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        commentRepository.delete(comment);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Comment deleted successfully")
                .build();
    }

    public AppResponse updateComment(String id, UpdateComment request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        request.getContent().ifPresent(comment::setContent);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Comment updated successfully")
                .data(commentRepository.save(comment))
                .build();
    }
}
