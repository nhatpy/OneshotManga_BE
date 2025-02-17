package com.anime_social.repository;

import com.anime_social.entity.FollowMangaListManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMangaListMangaRepository extends JpaRepository<FollowMangaListManga, String> {
}
