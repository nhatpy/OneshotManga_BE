package com.anime_social.repositorys;

import com.anime_social.models.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

    @Query("SELECT u FROM User u WHERE u.isVerified = true")
    List<User> findAllUserVerified(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u")
    Optional<Integer> getNumberOfUser();
}
