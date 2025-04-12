package com.anime_social.dto.response;

import java.util.Date;

import com.anime_social.models.UserReadManga;

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
public class HistoryListMangaResponse {
    Date lastReadAtDate;
    Integer lastReadAtChapter;
    MangaResponse manga;

    public static HistoryListMangaResponse toHistoryListResponse(UserReadManga userReadManga) {
        return HistoryListMangaResponse.builder()
                .lastReadAtDate(userReadManga.getLastReadAtDate())
                .lastReadAtChapter(userReadManga.getLastReadAtChapter())
                .manga(MangaResponse.toMangaResponse(userReadManga.getManga()))
                .build();
    }
}
