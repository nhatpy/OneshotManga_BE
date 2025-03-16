package com.anime_social.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "follow_manga_list_manga")
public class FollowMangaListManga {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @ManyToOne
    @JoinColumn(name = "follow_manga_list_id", nullable = false)
    FollowMangaList followMangaList;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    Manga manga;
}
