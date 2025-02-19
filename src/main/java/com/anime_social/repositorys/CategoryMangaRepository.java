package com.anime_social.repositorys;

import com.anime_social.models.CategoryManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMangaRepository extends JpaRepository<CategoryManga, String> {
}
