package com.team.identify.IdentifyAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import com.team.identify.IdentifyAPI.apps.crypt.utils.AESUtil;
import jakarta.persistence.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Entity
@Cacheable(value = false)
@Table(name = "ips_job_queue")
public class IPSJob {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    // image saved in flat file storage
    private String imageId;

    @JsonIgnore
    private String encryptionKey;

    private String ivB64;

    @JsonIgnore
    @ManyToOne
    private Company relatedCompany;

    @Enumerated(EnumType.STRING)
    private EJobState state;

    private String submissionIp;

    private Instant startTime;

    @ManyToOne
    private IDCollectionPoint collectionPoint;

    private String rabbitCorrelationID;

    @ManyToOne
    private IPS assignedIPS;

    @Column(columnDefinition="text")
    private String jobData;

    public IPSJob() {}

    public IPSJob(EJobState state, String submissionIp, IDCollectionPoint collectionPoint) {
        this.state = state;
        this.submissionIp = submissionIp;
        this.startTime = Instant.now();
        this.encryptionKey = AESUtil.getBase64FromKey(generateKey());
        this.collectionPoint = collectionPoint;
    }

    public IDCollectionPoint getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(IDCollectionPoint collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public String getRabbitCorrelationID() {
        return rabbitCorrelationID;
    }

    public String getJobData() {
        return jobData;
    }

    public void setJobData(String jobData) {
        this.jobData = jobData;
    }

    public void setRabbitCorrelationID(String rabbitCorrelationID) {
        this.rabbitCorrelationID = rabbitCorrelationID;
    }

    public IPS getAssignedIPS() {
        return assignedIPS;
    }

    public void setAssignedIPS(IPS assignedIPS) {
        this.assignedIPS = assignedIPS;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Company getRelatedCompany() {
        return relatedCompany;
    }

    public void setRelatedCompany(Company relatedCompany) {
        this.relatedCompany = relatedCompany;
    }

    public UUID getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public EJobState getState() {
        return state;
    }

    public void setState(EJobState state) {
        this.state = state;
    }

    public IPS getIps() {
        return assignedIPS;
    }

    public void setIps(IPS ips) {
        this.assignedIPS = ips;
    }

    public String getSubmissionIp() {
        return submissionIp;
    }

    public void setSubmissionIp(String submissionIp) {
        this.submissionIp = submissionIp;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public String getIvB64() {
        return ivB64;
    }

    public void setIvB64(String ivB64) {
        this.ivB64 = ivB64;
    }

    @JsonIgnore
    public SecretKey getKey() {
        return AESUtil.getAesKeyFromBase64(this.encryptionKey);
    }

    @JsonIgnore
    public IvParameterSpec getIV() {
        return new IvParameterSpec(Base64.getDecoder().decode(this.ivB64));
    }

    private Key generateKey() throws RuntimeException {
        try {
            this.ivB64 = Base64.getEncoder().encodeToString(AESUtil.generateIv().getIV());
            return AESUtil.getKeyFromKeyGenerator("AES", 256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No AES algorithm found");
        }

    }

    public boolean userIsAssigned(String username) {
        if (null == assignedIPS)
            return false;
        return Objects.equals(this.assignedIPS.getUser().getUsername(), username);
    }
}
