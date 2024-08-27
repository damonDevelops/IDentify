package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.SystemRole;
import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<SystemRole, Long> {
    Optional<SystemRole> findByName(ESystemRole name);
}
