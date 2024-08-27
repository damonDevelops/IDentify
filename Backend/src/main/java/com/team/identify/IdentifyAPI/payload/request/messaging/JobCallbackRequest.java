package com.team.identify.IdentifyAPI.payload.request.messaging;

public class JobCallbackRequest {
    private String ipsId;

    private int numJobs;

    private String jobId;

    public JobCallbackRequest(String ipsId, int numJobs, String jobId) {
        this.ipsId = ipsId;
        this.numJobs = numJobs;
        this.jobId = jobId;
    }

    public JobCallbackRequest() {

    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getIpsId() {
        return ipsId;
    }

    public void setIpsId(String ipsId) {
        this.ipsId = ipsId;
    }

    public int getNumJobs() {
        return numJobs;
    }

    public void setNumJobs(int numJobs) {
        this.numJobs = numJobs;
    }
}
