package com.anime_social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anime_social.models.ChatbotHistory;

@Repository
public interface ChatbotRepository extends JpaRepository<ChatbotHistory, String> {
}
