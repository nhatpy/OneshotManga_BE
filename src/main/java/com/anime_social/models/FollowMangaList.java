package com.anime_social.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "follow_manga_list")
public class FollowMangaList extends BaseEntity {
    @Id
    @Column(name = "id")
    String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    User user;

    @OneToMany(mappedBy = "followMangaList", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<FollowMangaListManga> followMangaListMangas;
}
