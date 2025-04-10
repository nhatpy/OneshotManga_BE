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

    @Query("SELECT m FROM Manga m WHERE m.isActive = true")
    List<Manga> findAllPagingWithActive(Pageable pageable);

    @Query("SELECT COUNT(m) FROM Manga m WHERE m.isActive = true")
    Optional<Integer> getNumberOfAllActive();

    @Query("SELECT COUNT(m) FROM Manga m")
    Optional<Integer> getNumberOfAll();

    @Query("SELECT m FROM Manga m WHERE m.author.id = :authorId")
    List<Manga> findAllByAuthorId(@Param("authorId") String authorId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Manga m WHERE m.author.id = :authorId")
    Optional<Integer> countByAuthorId(@Param("authorId") String authorId);
}
