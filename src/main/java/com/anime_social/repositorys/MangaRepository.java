package com.anime_social.repositorys;

import com.anime_social.models.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MangaRepository extends JpaRepository<Manga, String> {
    Manga findBySlug(String slug);
}
