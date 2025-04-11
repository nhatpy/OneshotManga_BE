package com.anime_social.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anime_social.models.MangaInteraction;

@Repository
public interface MangaInteractionRepository extends JpaRepository<MangaInteraction, String> {
    @Query("""
            SELECT mi FROM MangaInteraction mi
            WHERE mi.updateAt >= :startOfDay
            ORDER BY mi.updateAt DESC
            """)
    List<MangaInteraction> findTopDayManga(
            @Param("startOfDay") Date startOfDay,
            Pageable pageable);

}
