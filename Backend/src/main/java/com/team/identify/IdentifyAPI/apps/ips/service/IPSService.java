package com.team.identify.IdentifyAPI.apps.ips.service;

import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.IPSJobRepository;
import com.team.identify.IdentifyAPI.database.IPSRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.model.IPSJob;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.EIPSState;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import com.team.identify.IdentifyAPI.util.exception.IPSUserIDException;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class IPSService {
    private static final Logger logger = LoggerFactory.getLogger(IPSService.class);

    @Value("${flatfileStorage.path:/tmp/}")
    private String storageRoot;

    @Autowired
    IPSRepository ipsRepo;

    @Autowired
    IPSJobRepository ipsJobRepo;

    @Autowired
    CardDataRepository cardRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    EntityManager entityManager;

    @Autowired
    JobScheduler scheduler;

    @PostConstruct
    private void initScheduledJobs() {
        scheduler.scheduleRecurrently(Cron.every30seconds(), this::serverHeartbeatMonitor);
    }

    public IPS registerServer(int maxJobs, Long user_id) throws IPSUserIDException, IdNotFoundError {
        if (ipsRepo.userIdExists(user_id)) {
            // .get is fine we just checked
            IPS ips = ipsRepo.getIpsByUser(userRepo.findById(user_id).get()).get();
            if (ips.getState() != EIPSState.DOWN) {
                throw new IPSUserIDException(user_id);
            } else {
                ips.setState(EIPSState.UP);
                ipsRepo.save(ips);
                return ips;
            }
        }

        User user = userRepo.findById(user_id).orElseThrow(
                () -> new IdNotFoundError(user_id.toString())
        );
        IPS server = new IPS(maxJobs, EIPSState.UP, user);

        ipsRepo.save(server);
        return server;
    }

    public void deleteServer(String id) throws IdNotFoundError {
        IPS server = ipsRepo.findByIdString(id).orElseThrow(
                () -> new IdNotFoundError(id)
        );
        ipsRepo.delete(server);
    }

    public void processShutdownMessage(String ipsId) throws IdNotFoundError {
        IPS server = ipsRepo.findByIdString(ipsId).orElseThrow(
                () -> new IdNotFoundError(ipsId)
        );

        if (server.getState() == EIPSState.DOWN) {
            return;
        }

        server.setState(EIPSState.DOWN);
        ipsRepo.save(server);

        List<IPSJob> jobs = ipsJobRepo.getIpsJobsByAssignedIPS(server);
        for(IPSJob job : jobs) {
            if(job.getState() == EJobState.ASSIGNED ||
                    job.getState() == EJobState.PROCESSING) {
                job.setState(EJobState.NEW);
                job.setIps(null);
                ipsJobRepo.save(job);
            }
        }
    }

    public void serverHeartbeatMonitor() {
        // for all IPS we haven't seen in 30 sec, consider it dead and call the shutdown function
        getLastSeenTimes().forEach(
                (uuid, time) -> {
                    if (time.isBefore(Instant.now().minus(30, ChronoUnit.SECONDS))) {
                        try {
                            this.processShutdownMessage(uuid.toString());
                        } catch (IdNotFoundError ignored) {
                            // something went wrong if the IPS doesn't exist anymore.
                        }
                    }
                }
        );
    }

    private HashMap<UUID, Instant> getLastSeenTimes() {
        HashMap<UUID, Instant> toReturn = new HashMap<>();
        Query query = entityManager.createNativeQuery(
                "SELECT relatedips_id, MAX(time) FROM ips_stats GROUP BY relatedips_id"
        );
        List<Object> results = query.getResultList();
        for (Object result: results) {
            if (result instanceof Object[]) {
                Object[] arr = (Object[]) result;
                toReturn.put((UUID) arr[0], (Instant) arr[1]);
            }
        }
        return toReturn;
    }
}
