package com.anime_social.dto.response;

import com.anime_social.models.FollowMangaListManga;

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
public class FollowListMangaResponse {
    String id;
    MangaResponse manga;

    public static FollowListMangaResponse toFollowListResponse(FollowMangaListManga followList) {
        return FollowListMangaResponse.builder()
                .id(followList.getFollowMangaList().getId())
                .manga(MangaResponse.toMangaResponse(followList.getManga()))
                .build();
    }
}
