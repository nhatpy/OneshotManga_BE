package com.anime_social.repositorys;

import com.anime_social.models.Chapter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    Optional<Chapter> findByChapterNumber(Integer chapterNumber);
}
