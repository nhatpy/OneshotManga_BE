package com.anime_social.repositories;

import com.anime_social.models.VerifyUserCode;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyUserCodeRepository extends JpaRepository<VerifyUserCode, String> {
    Optional<VerifyUserCode> findByCode(String code);
}
