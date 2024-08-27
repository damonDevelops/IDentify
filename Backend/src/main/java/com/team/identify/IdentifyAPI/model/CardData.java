package com.team.identify.IdentifyAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "card_data")
public class CardData {
    @Id
    public UUID jobId;

    public String submissionIP;

    @NotNull
    @JsonIgnore
    @Type(value = com.team.identify.IdentifyAPI.model.types.EncryptedRowType.class)
    @Column(columnDefinition = "text")
    private EncryptedRow encryptedCardData;

    @ManyToOne
    private IDCollectionPoint collectionPoint;

    private Instant collectedOn;

    public CardData() {}

    public CardData(UUID id, String submissionIP, IDCollectionPoint collectionPoint, EncryptedRow encryptedRow) {
        this.jobId = id;
        this.submissionIP = submissionIP;
        this.collectionPoint = collectionPoint;
        this.encryptedCardData = encryptedRow;
        this.collectedOn = Instant.now();
    }


    public Instant getCollectedOn() {
        return collectedOn;
    }


    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public EncryptedRow getEncryptedCardData() {
        return encryptedCardData;
    }

    public void setEncryptedCardData(EncryptedRow encryptedCardData) {
        this.encryptedCardData = encryptedCardData;
    }

    public IDCollectionPoint getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(IDCollectionPoint collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public String getSubmissionIP() {
        return submissionIP;
    }

    public void setSubmissionIP(String submissionIP) {
        this.submissionIP = submissionIP;
    }
}
