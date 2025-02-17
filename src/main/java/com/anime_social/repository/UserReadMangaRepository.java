package com.anime_social.repository;

import com.anime_social.entity.UserReadManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReadMangaRepository extends JpaRepository<UserReadManga, String> {
}
