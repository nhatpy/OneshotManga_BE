package com.anime_social.dto.response;

import com.anime_social.models.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String content;
    UserResponse user;
    ChapterResponse chapter;

    public static CommentResponse toCommentResponse(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserResponse.toUserResponse(comment.getUser()))
                .chapter(ChapterResponse.toChapterResponse(comment.getChapter()))
                .build();
    }
}
