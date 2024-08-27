package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.IPSStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPSStatsRepository extends JpaRepository<IPSStats, UUID> {
}
