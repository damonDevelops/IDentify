package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.Authority;
import com.team.identify.IdentifyAPI.model.enums.EAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Optional<Authority> findByName(EAuthority name);

}
