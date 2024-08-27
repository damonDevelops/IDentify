package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.CompanyRole;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findUsersByCompanyRolesContains(CompanyRole role);

    @Query("SELECT u FROM User u LEFT JOIN IDCollectionPoint cp ON cp = ?1")
    List<User> findUsersByCollectionPoint(IDCollectionPoint point);

    Optional<User> findUserByVerificationToken(UUID token);
}
