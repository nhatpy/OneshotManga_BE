package com.anime_social.repositories;

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

    @Query("SELECT u FROM User u WHERE u.isVerified = true AND u.fullName != 'admin'")
    List<User> findAllUserVerified(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isVerified = true AND u.fullName != 'admin'")
    Optional<Integer> getNumberOfUserVerified();

    @Query("SELECT u FROM User u WHERE u.isVerified = true AND u.fullName != 'admin' AND u.wallet != 0")
    List<User> findAllTopUser(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isVerified = true AND u.fullName != 'admin' AND u.wallet != 0")
    Optional<Integer> getNumberOfTopUserVerified();
}
