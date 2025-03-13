package com.anime_social.repositorys;

import com.anime_social.models.FollowMangaListManga;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMangaListMangaRepository extends JpaRepository<FollowMangaListManga, String> {
    @Query("SELECT fmlm FROM FollowMangaListManga fmlm WHERE fmlm.followMangaList.id = :followMangaListId AND fmlm.manga.id=:mangaId")
    Optional<FollowMangaListManga> findByFollowMangaListIdAndMangaId(
            @Param("followMangaListId") String followMangaListId, @Param("mangaId") String mangaId);
}
