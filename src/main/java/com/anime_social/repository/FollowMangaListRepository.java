package com.anime_social.repository;

import com.anime_social.entity.FollowMangaList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMangaListRepository extends JpaRepository<FollowMangaList, String> {
}
