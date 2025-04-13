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
    String mangaId;
    String mangaSlug;
    String mangaName;
    List<Integer> numberOfChapter;
    Date createAt;
    Date updateAt;
    Integer numberOfComment;

    public static ChapterResponse toChapterResponse(Chapter chapter, List<Integer> numberOfChapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .chapterNumber(chapter.getChapterNumber())
                .content(chapter.getContent())
                .mangaId(chapter.getManga().getId())
                .mangaSlug(chapter.getManga().getSlug())
                .mangaName(chapter.getManga().getName())
                .numberOfComment(Optional.ofNullable(chapter.getComments()).orElse(Collections.emptyList()).size())
                .numberOfChapter(numberOfChapter)
                .createAt(chapter.getCreateAt())
                .updateAt(chapter.getUpdateAt())
                .build();
    }
}
