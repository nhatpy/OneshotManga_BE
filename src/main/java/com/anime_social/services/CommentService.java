package com.anime_social.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.PostComment;
import com.anime_social.dto.request.UpdateComment;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.CommentResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
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
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                Chapter chapter = chapterRepository.findById(request.getChapterId())
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.CHAPTER_NOT_FOUND));

                Comment comment = Comment.builder()
                                .content(request.getContent())
                                .user(user)
                                .chapter(chapter)
                                .build();

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Bạn đã để lại một bình luận")
                                .data(CommentResponse.toCommentResponse(commentRepository.save(comment)))
                                .build();
        }

        public AppResponse deleteComment(String id) {
                Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.COMMENT_NOT_FOUND));

                commentRepository.delete(comment);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã xóa bình luận")
                                .build();
        }

        public AppResponse updateComment(String id, UpdateComment request) {
                Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.COMMENT_NOT_FOUND));

                request.getContent().ifPresent(comment::setContent);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã cập nhật bình luận")
                                .data(CommentResponse.toCommentResponse(commentRepository.save(comment)))
                                .build();
        }
}
