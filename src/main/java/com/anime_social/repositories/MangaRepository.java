package com.anime_social.repositories;

import com.anime_social.models.Manga;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MangaRepository extends JpaRepository<Manga, String> {
        Optional<Manga> findBySlug(String slug);

        @Query("SELECT m FROM Manga m WHERE m.author.id = :authorId")
        List<Manga> findAllByAuthorId(@Param("authorId") String authorId, Pageable pageable);

        @Query("SELECT COUNT(m) FROM Manga m WHERE m.author.id = :authorId")
        Optional<Integer> countByAuthorId(@Param("authorId") String authorId);

        @Query("""
                        SELECT DISTINCT m
                        FROM Manga m
                        LEFT JOIN m.categoryMangas cm
                        LEFT JOIN cm.category c
                        WHERE (:searchQuery IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
                            AND (:categorySlug IS NULL OR c.slug = :categorySlug)
                            AND (:status IS NULL OR m.isDone = :status)
                            AND (:active IS NULL OR m.isActive = :active)
                        """)
        List<Manga> findFilterManga(
                        @Param("active") Boolean active,
                        @Param("searchQuery") String searchQuery,
                        @Param("categorySlug") String categorySlug,
                        @Param("status") Boolean status,
                        Pageable pageable);

        @Query("""
                        SELECT COUNT(DISTINCT m)
                        FROM Manga m
                        LEFT JOIN m.categoryMangas cm
                        LEFT JOIN cm.category c
                        WHERE (:searchQuery IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
                            AND (:categorySlug IS NULL OR c.slug = :categorySlug)
                            AND (:status IS NULL OR m.isDone = :status)
                            AND (:active IS NULL OR m.isActive = :active)
                        """)
        Optional<Integer> countFilterManga(
                        @Param("active") Boolean active,
                        @Param("searchQuery") String searchQuery,
                        @Param("categorySlug") String categorySlug,
                        @Param("status") Boolean status);

        @Query("""
                        SELECT DISTINCT m
                        FROM Manga m
                        LEFT JOIN m.categoryMangas cm
                        LEFT JOIN cm.category c
                        LEFT JOIN m.chapters ch
                        WHERE (:searchQuery IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
                            AND (:categorySlug IS NULL OR c.slug = :categorySlug)
                            AND (:status IS NULL OR m.isDone = :status)
                            AND (:active IS NULL OR m.isActive = :active)
                        GROUP BY m
                        ORDER BY COUNT(DISTINCT ch.id) DESC
                        """)
        List<Manga> findMangaSortByNumberOfChapter(
                        @Param("active") Boolean active,
                        @Param("searchQuery") String searchQuery,
                        @Param("categorySlug") String categorySlug,
                        @Param("status") Boolean status,
                        Pageable pageable);

        @Query("""
                        SELECT DISTINCT m
                        FROM Manga m
                        LEFT JOIN m.categoryMangas cm
                        LEFT JOIN cm.category c
                        LEFT JOIN m.chapters ch
                        LEFT JOIN ch.comments co
                        WHERE (:searchQuery IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
                            AND (:categorySlug IS NULL OR c.slug = :categorySlug)
                            AND (:status IS NULL OR m.isDone = :status)
                            AND (:active IS NULL OR m.isActive = :active)
                        GROUP BY m
                        ORDER BY COUNT(DISTINCT co.id) DESC
                        """)
        List<Manga> findMangaSortByNumberOfComment(
                        @Param("active") Boolean active,
                        @Param("searchQuery") String searchQuery,
                        @Param("categorySlug") String categorySlug,
                        @Param("status") Boolean status,
                        Pageable pageable);
}
