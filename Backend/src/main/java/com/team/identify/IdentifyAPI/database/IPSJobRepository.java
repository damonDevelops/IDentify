package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.model.IPSJob;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPSJobRepository extends JpaRepository<IPSJob, UUID> {
    default Optional<IPSJob> findByIdString(String id) {
        return this.findById(UUID.fromString(id));
    }

    List<IPSJob> getIpsJobsByAssignedIPS(IPS assignedServer);
//
//    @Query(value = "UPDATE IPSJob SET ips = ?2 WHERE ips = ?1")
//    void reassignJobs(IPS fromIps, IPS targetIps);
//
//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM IPSJob WHERE ips = ?1")
//    void dropJobs(IPS ips);
//
//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE IPSJob SET ips = ?1, state = com.team.identify.IdentifyAPI.model.enums.EJobState.WAITING WHERE state = com.team.identify.IdentifyAPI.model.enums.EJobState.UNASSIGNED")
//    void assignUnassignedJobs(IPS ips);

    List<IPSJob> getAllByCollectionPoint(IDCollectionPoint collectionPoint);

    List<IPSJob> getIPSJobsByState(EJobState state);

}
