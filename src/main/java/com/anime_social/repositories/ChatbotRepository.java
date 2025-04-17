package com.anime_social.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anime_social.models.ChatbotHistory;

@Repository
public interface ChatbotRepository extends JpaRepository<ChatbotHistory, String> {
    boolean existsByMessage(String message);

    @Query("SELECT c.message FROM ChatbotHistory c WHERE c.user.id = :userId")
    List<String> findAllMessageByUserId(@Param("userId") String userId, Pageable pageable);
}
