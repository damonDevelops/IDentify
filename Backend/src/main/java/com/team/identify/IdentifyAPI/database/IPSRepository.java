package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IPSRepository extends JpaRepository<IPS, UUID> {
    default Optional<IPS> findByIdString(String id) {
        return this.findById(UUID.fromString(id));
    }

    @Query(value = "SELECT COUNT(1) > 0 FROM ips_server WHERE user_id=?1", nativeQuery = true)
    boolean userIdExists(Long user_id);

    Optional<IPS> getIpsByUser(User user);

    @Query( value = """
    SELECT *
    FROM ips_server
    WHERE id = (SELECT ips_id
            FROM ips_job_queue
            WHERE ips_id != ?1
            GROUP BY ips_id
            ORDER BY count(*) ASC
            LIMIT 1)
    """,
    nativeQuery = true)
    Optional<IPS> getJobReassignmentCandidate(UUID ips_id);
}
