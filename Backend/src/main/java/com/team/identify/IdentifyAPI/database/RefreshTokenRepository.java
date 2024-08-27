package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.RefreshToken;
import com.team.identify.IdentifyAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    //modifying includes INSERT, DELETE and UPDATE operations
    //returns an int for number of matched changes
    @Modifying
    int deleteByUser(User user);
}