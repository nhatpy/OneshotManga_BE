package com.anime_social.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.anime_social.repositories.ChapterRepository;
import com.anime_social.repositories.CommentRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
        private final CommentRepository commentRepository;
        private final ChapterRepository chapterRepository;
        private final UserRepository userRepository;

        @Override
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

        @Override
        public AppResponse deleteComment(String id) {
                Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.COMMENT_NOT_FOUND));

                commentRepository.delete(comment);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã xóa bình luận")
                                .build();
        }

        @Override
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

        @Override
        public AppResponse getCommentByChapterId(String chapterId, int page, int size) {
                int staterPage = page - 1;
                Pageable pageable = PageRequest.of(staterPage, size);
                List<Comment> comments = commentRepository.findByChapterId(chapterId, pageable);

                List<CommentResponse> commentResponses = comments.stream()
                                .map(CommentResponse::toCommentResponse)
                                .toList();
                return AppResponse.builder()
                                .data(commentResponses)
                                .status(HttpStatus.OK)
                                .message("Lấy bình luận thành công")
                                .build();
        }
}
