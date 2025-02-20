package com.anime_social.repositorys;

import com.anime_social.models.UserReadManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReadMangaRepository extends JpaRepository<UserReadManga, String> {
}
