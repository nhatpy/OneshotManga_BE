package com.anime_social.repositorys;

import com.anime_social.models.FollowMangaList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMangaListRepository extends JpaRepository<FollowMangaList, String> {
}
