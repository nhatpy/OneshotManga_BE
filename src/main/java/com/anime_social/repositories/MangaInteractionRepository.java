package com.anime_social.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.anime_social.models.MangaInteraction;

@Repository
public interface MangaInteractionRepository extends JpaRepository<MangaInteraction, String> {
        @Query("""
                        SELECT mi FROM MangaInteraction mi
                        ORDER BY mi.time DESC
                        """)
        List<MangaInteraction> findTopDayManga(
                        Pageable pageable);

        @Query("""
                        SELECT COUNT(mi) FROM MangaInteraction mi
                        """)
        Optional<Integer> countTopDayManga();

}
