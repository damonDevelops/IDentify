package com.team.identify.IdentifyAPI.payload.response;

public class SubmissionResponse {
    public String jobId;
    public Integer eta;

    public SubmissionResponse(String jobId, Integer eta) {
        this.jobId = jobId;
        this.eta = eta;
    }
}
