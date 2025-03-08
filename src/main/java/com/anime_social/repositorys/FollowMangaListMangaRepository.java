package com.anime_social.repositorys;

import com.anime_social.models.FollowMangaListManga;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMangaListMangaRepository extends JpaRepository<FollowMangaListManga, String> {
    Optional<FollowMangaListManga> findByFollowMangaListIdAndMangaId(String followMangaListId, String mangaId);
}
