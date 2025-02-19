package com.anime_social.repositorys;

import com.anime_social.models.VerifyUserCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyUserCodeRepository extends JpaRepository<VerifyUserCode, String> {
    public VerifyUserCode findByCode(String code);
}
