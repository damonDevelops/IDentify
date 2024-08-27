package com.team.identify.IdentifyAPI.payload.response;

public class JobDataResponse {
    private String jobId;
    private String imageB64;


    public JobDataResponse(String jobId, String imageB64) {
        this.jobId = jobId;
        this.imageB64 = imageB64;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getImageB64() {
        return imageB64;
    }

    public void setImageB64(String imageB64) {
        this.imageB64 = imageB64;
    }
}
