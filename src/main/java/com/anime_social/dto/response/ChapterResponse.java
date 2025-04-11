package com.anime_social.dto.response;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.anime_social.models.Chapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChapterResponse {
    String id;
    Integer chapterNumber;
    List<String> content;
    String mangaSlug;
    Date createAt;
    Date updateAt;
    Integer numberOfComment;

    public static ChapterResponse toChapterResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .chapterNumber(chapter.getChapterNumber())
                .content(chapter.getContent())
                .mangaSlug(chapter.getManga().getSlug())
                .numberOfComment(Optional.ofNullable(chapter.getComments()).orElse(Collections.emptyList()).size())
                .createAt(chapter.getCreateAt())
                .updateAt(chapter.getUpdateAt())
                .build();
    }
}
