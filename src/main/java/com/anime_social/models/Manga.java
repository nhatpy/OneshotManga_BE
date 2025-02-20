package com.anime_social.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "manga")
public class Manga extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "cover_image")
    String coverImage;

    @Column(name = "view")
    Integer view;

    @Column(name = "follow")
    Integer follow;

    @Column(name = "is_done")
    @Builder.Default
    Boolean isDone = false;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manga", cascade = CascadeType.ALL)
    List<CategoryManga> categoryMangas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manga", cascade = CascadeType.ALL)
    List<Chapter> chapters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manga", cascade = CascadeType.ALL)
    List<UserReadManga> userReadMangas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manga", cascade = CascadeType.ALL)
    List<FollowMangaListManga> followMangaListMangas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    User author;
}
