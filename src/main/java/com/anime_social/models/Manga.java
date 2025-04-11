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

    @Column(name = "name", unique = true)
    String name;

    @Column(name = "slug", unique = true)
    String slug;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    String description;

    @Column(name = "cover_image")
    String coverImage;

    @Column(name = "view")
    @Builder.Default
    Integer view = 0;

    @Column(name = "follow")
    @Builder.Default
    Integer follow = 0;

    @Column(name = "is_done")
    Boolean isDone;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = false;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    List<CategoryManga> categoryMangas;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    List<Chapter> chapters;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    List<UserReadManga> userReadMangas;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL)
    List<FollowMangaListManga> followMangaListMangas;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @OneToOne(mappedBy = "manga", cascade = CascadeType.ALL)
    MangaInteraction mangaInteraction;
}
