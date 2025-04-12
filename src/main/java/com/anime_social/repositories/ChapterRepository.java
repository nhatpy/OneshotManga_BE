package com.anime_social.repositories;

import com.anime_social.models.Chapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    @Query("SELECT c FROM Chapter c WHERE c.manga.slug = :slug AND c.chapterNumber=:chapterNumber")
    Optional<Chapter> findByChapterNumberAndMangaSlug(
            @Param("slug") String slug,
            @Param("chapterNumber") Integer chapterNumber);

    @Query("SELECT c FROM Chapter c WHERE c.manga.slug = :mangaSlug")
    List<Chapter> findByMangaSlug(@Param("mangaSlug") String mangaSlug, Sort sort);
}
