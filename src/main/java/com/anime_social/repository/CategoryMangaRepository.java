package com.anime_social.repository;

import com.anime_social.entity.CategoryManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMangaRepository extends JpaRepository<CategoryManga, String> {
}
