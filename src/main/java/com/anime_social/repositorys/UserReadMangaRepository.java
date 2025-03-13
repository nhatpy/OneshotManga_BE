package com.anime_social.repositorys;

import com.anime_social.models.UserReadManga;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReadMangaRepository extends JpaRepository<UserReadManga, String> {
    @Query("SELECT urm FROM UserReadManga urm WHERE urm.user.id = :userId AND urm.manga.id = :mangaId")
    Optional<UserReadManga> findByUserIdAndMangaId(@Param("userId") String userId, @Param("mangaId") String mangaId);
}
