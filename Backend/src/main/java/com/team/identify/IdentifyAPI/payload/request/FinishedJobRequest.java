package com.team.identify.IdentifyAPI.payload.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.team.identify.IdentifyAPI.apps.ips.pojo.EJobFailureReason;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Internal DTO used for the IPS")
public class FinishedJobRequest {
    public String jobId;
    public String cardType;

    public JsonNode cardData;

    public EJobFailureReason error;

    public FinishedJobRequest() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public JsonNode getCardData() {
        return cardData;
    }

    public void setCardData(JsonNode cardData) {
        this.cardData = cardData;
    }

    public EJobFailureReason getError() {
        return error;
    }

    public void setError(EJobFailureReason error) {
        this.error = error;
    }
}
