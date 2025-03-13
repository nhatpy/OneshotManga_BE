package com.anime_social.repositorys;

import com.anime_social.models.Category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);

    @Query("SELECT COUNT(c) FROM Category c")
    Integer getNumberOfAll();
}
