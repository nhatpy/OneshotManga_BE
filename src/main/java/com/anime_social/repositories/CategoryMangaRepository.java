package com.anime_social.repositories;

import com.anime_social.models.CategoryManga;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMangaRepository extends JpaRepository<CategoryManga, String> {
    @Query("""
                SELECT c.name, COUNT(cm.manga.id) as mangaCount
                FROM CategoryManga cm
                JOIN cm.category c
                GROUP BY c.id, c.name
                ORDER BY mangaCount DESC
            """)
    List<Object[]> getTopCategoriesByMangaCount();

    @Query("""
                SELECT COUNT(DISTINCT cm.manga.id) as mangaCount
                FROM CategoryManga cm
            """)
    Optional<Integer> getNumberOfManga();

    @Query("""
                SELECT COUNT(DISTINCT cm.category.id) as mangaCount
                FROM CategoryManga cm
            """)
    Optional<Integer> getNumberOfCategory();
}
