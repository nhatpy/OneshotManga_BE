package com.anime_social.repositories;

import com.anime_social.models.Chapter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    @Query("SELECT c FROM Chapter c WHERE c.manga.id = :mangaId AND c.chapterNumber=:chapterNumber")
    Optional<Chapter> findByChapterNumberAndMangaId(
            @Param("mangaId") String mangaId,
            @Param("chapterNumber") Integer chapterNumber);
}
