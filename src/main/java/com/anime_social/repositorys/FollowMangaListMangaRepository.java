package com.anime_social.repositorys;

import com.anime_social.models.FollowMangaListManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMangaListMangaRepository extends JpaRepository<FollowMangaListManga, String> {
}
