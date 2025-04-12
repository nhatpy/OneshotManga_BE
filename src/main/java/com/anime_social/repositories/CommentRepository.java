package com.anime_social.repositories;

import com.anime_social.models.Comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("SELECT c FROM Comment c WHERE c.chapter.id = :chapterId")
    List<Comment> findByChapterId(@Param("chapterId") String chapterId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.chapter.id = :chapterId")
    Optional<Integer> countByChapterId(@Param("chapterId") String chapterId);
}
