package com.team.identify.IdentifyAPI.apps.ips.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team.identify.IdentifyAPI.apps.crypt.utils.AESUtil;
import com.team.identify.IdentifyAPI.apps.ips.pojo.EJobFailureReason;
import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.IPSJobRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.IPSJob;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import com.team.identify.IdentifyAPI.payload.request.FinishedJobRequest;
import com.team.identify.IdentifyAPI.util.FlatfileStorage;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import com.team.identify.IdentifyAPI.util.exception.UserNotFoundException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class IPSJobService {

    private static final Logger logger = LoggerFactory.getLogger(IPSJobService.class);

    private final UserRepository userRepository;
    private final CardDataRepository cardDataRepository;

    @Value("${flatfileStorage.path:/tmp/}")
    private String storageRoot;

    private final IPSJobRepository ipsJobRepo;


    public IPSJobService(IPSJobRepository ipsJobRepo, UserRepository userRepository, CardDataRepository cardDataRepository) {
        this.ipsJobRepo = ipsJobRepo;
        this.userRepository = userRepository;
        this.cardDataRepository = cardDataRepository;
    }


    /**
     * Use this function to send work to the IPS servers
     * @param image bytes of image, in png format
     * @param submissionIp string, IP address of submitter
     * @param collectionPoint IDCollectionPoint, the source of this request
     * @return IPSJob, new job, persisted
     */
    public IPSJob submitJob(byte[] image, String submissionIp, IDCollectionPoint collectionPoint) throws IOException {
        // create ipsJob to generate keys
        IPSJob newJob = new IPSJob(EJobState.NEW, submissionIp, collectionPoint);
        // encrypt image content
        byte[] encryptedImage = AESUtil.encryptByteArray(image, newJob.getKey(), newJob.getIV());
        // save encrypted image to disk
        String imageId = FlatfileStorage.saveFile(encryptedImage, storageRoot);

        newJob.setImageId(imageId);

        ipsJobRepo.save(newJob);

        return newJob;
    }

    public EJobState getJobState(String jobId) {
        Optional<IPSJob> job = ipsJobRepo.findByIdString(jobId);
        if (job.isPresent()) {
            return job.get().getState();
        }

        Optional<CardData> card = cardDataRepository.findById(UUID.fromString(jobId));
        if (card.isPresent()) {
            return EJobState.COMPLETE;
        } else {
            return EJobState.MISSING;
        }
    }



    /**
     * Returns encryption keys for the job if the given user is allowed to access them
     * @param job job request pertains to
     * @param user user who is making the request
     * @return An immutable pair, with the first value the encryption key, second the iv (b64 encoded)
     */
    public ImmutablePair<String, String> getEncryptionKeys(IPSJob job, User user) {
        if (!userAccessAllowed(job, user))
            throw new AccessDeniedException("You are not permitted to access this resource.");

        return new ImmutablePair<>(job.getEncryptionKey(), job.getIvB64());
    }

    public ImmutablePair<String, String> getEncryptionKeys(String jobId, String username) throws IdNotFoundError {
        IPSJob job = ipsJobRepo.findByIdString(jobId).orElseThrow(
                () -> new IdNotFoundError(jobId)
        );

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username, "username")
        );


        job.setState(EJobState.PROCESSING);
        ipsJobRepo.save(job);

        return this.getEncryptionKeys(job, user);
    }

    /**
     * Saves job data in the format [card type];[base64 encoded json]
     * @param jobId
     * @param req
     */
    public void finishedJob(String jobId, FinishedJobRequest req) throws IdNotFoundError {
        String b64CardDataJson = Base64.getEncoder().encodeToString(req.getCardData().toString().getBytes(StandardCharsets.UTF_8));

        ObjectMapper cardData = new ObjectMapper();
        ObjectNode rootNode = cardData.createObjectNode();
        rootNode.put("cardType", req.cardType);
        rootNode.set("cardData", req.getCardData());

        IPSJob job = ipsJobRepo.findByIdString(jobId).orElseThrow(
                () -> new IdNotFoundError(jobId)
        );
        try {
            job.setJobData(cardData.writer().writeValueAsString(rootNode));
        } catch (JsonProcessingException e) {
            logger.error("Exception when converting card data to json string: " + e);
            job.setState(EJobState.COMPLETE_WITH_ERROR);
            ipsJobRepo.save(job);
            return;
        }
        job.setState(EJobState.COMPLETE);
        ipsJobRepo.save(job);
        FlatfileStorage.deleteFile(job.getImageId(), storageRoot);
    }

    public void jobError(String jobId, EJobFailureReason error) {
        try {
            IPSJob job = ipsJobRepo.findByIdString(jobId).orElseThrow(
                    () -> new IdNotFoundError(jobId)
            );
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("error", error.toString());
            job.setJobData(mapper.writeValueAsString(root));
            job.setState(EJobState.FAILED);
            ipsJobRepo.save(job);
        } catch (IdNotFoundError e) {
            //TODO: handle error
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJobError(IPSJob job) {
        if (job.getJobData().startsWith("{")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(job.getJobData());
                return node.get("error").toString();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "";
        }
    }


    private boolean userAccessAllowed(IPSJob ipsJob, User user) {
        return user.isSuperUser() || Objects.equals(user.getId(), ipsJob.getIps().getUser().getId());
    }

}
