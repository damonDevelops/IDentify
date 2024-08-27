package com.team.identify.IdentifyAPI.apps.ips.service;

import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import com.team.identify.IdentifyAPI.apps.crypt.service.EncryptionService;
import com.team.identify.IdentifyAPI.apps.ips.messaging.publisher.JobPublisher;
import com.team.identify.IdentifyAPI.apps.ips.pojo.EJobFailureReason;
import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.IPSJobRepository;
import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.IPSJob;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import com.team.identify.IdentifyAPI.payload.response.JobDataResponse;
import com.team.identify.IdentifyAPI.util.FlatfileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class IPSJobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(IPSJobScheduler.class);


    private final com.team.identify.IdentifyAPI.database.IPSJobRepository ipsJobRepo;
    private final JobPublisher jobPublisher;
    private final IPSJobService ipsJobService;
    private final EncryptionService encryptionService;
    private final CardDataRepository cardDataRepository;

    @Value("${flatfileStorage.path:/tmp/}")
    private String storageRoot;



    public IPSJobScheduler(IPSJobRepository IPSJobRepository, JobPublisher jobPublisher, IPSJobService ipsJobService, EncryptionService encryptionService, CardDataRepository cardDataRepository) {
        this.ipsJobRepo = IPSJobRepository;
        this.jobPublisher = jobPublisher;
        this.ipsJobService = ipsJobService;
        this.encryptionService = encryptionService;
        this.cardDataRepository = cardDataRepository;
    }

    /**
     * Scheduled job that will queue submitted ipsJobs
     */
    @Scheduled(fixedDelay = 1000)
    public void queueUnassignedJobs() {
        for (IPSJob job : ipsJobRepo.getIPSJobsByState(EJobState.NEW)) {
            String jobId = job.getId().toString();
            String imageId = job.getImageId();
            try {
                // get and encode image
                byte[] encryptedImage = FlatfileStorage.getFileById(imageId, storageRoot);
                String imageB64 = Base64.getEncoder().encodeToString(encryptedImage);
                // send message
                jobPublisher.publishJob(new JobDataResponse(jobId, imageB64));

            } catch (IOException | InvalidPathException e) {
                logger.warn("Exception when retrieving file from storage: \n" + e);
                if (InvalidPathException.class == e.getClass())
                    FlatfileStorage.deleteFile(imageId, storageRoot);
                ipsJobService.jobError(jobId, EJobFailureReason.STORAGE_FAILURE);
                return;
            }
            job.setState(EJobState.UNASSIGNED);
            ipsJobRepo.save(job);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void processFinishedJobs() {
        for (IPSJob job : ipsJobRepo.getIPSJobsByState(EJobState.COMPLETE)) {
            ArrayList<PublicKey> allowedKeys = new ArrayList<>();
            for (User user : job.getCollectionPoint().getAccessList()) {
                allowedKeys.add(user.getPublicKey());
            }
            EncryptedRow crypt = encryptionService.encryptData(allowedKeys, job.getJobData().getBytes(StandardCharsets.UTF_8));
            CardData finishedJob = new CardData(job.getId(), job.getSubmissionIp(), job.getCollectionPoint(), crypt);
            cardDataRepository.save(finishedJob);
            ipsJobRepo.delete(job);
        }
    }

}
