package com.team.identify.IdentifyAPI.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team.identify.IdentifyAPI.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO for basic info about an ID collection point")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class IDCollectionPointInfoResponse {

    @Schema(
            description = "link to view all results from the endpoint"
    )
    private String url;

    @Schema(
            description = "The endpoint for submitting images to this endpoint"
    )
    private String submissionUrl;

    @Schema(
            description = "Number of valid card records that exist in the collection point"
    )
    private int numResults;

    @Schema(
        description = "Only returned if the current authentication principal has EDIT_COMPANY permission"
    )
    private List<User> userAccessList;

    public IDCollectionPointInfoResponse(String url, String submissionUrl, int numResults) {
        this.url = url;
        this.numResults = numResults;
        this.submissionUrl = submissionUrl;
    }

    public IDCollectionPointInfoResponse(String url, String submissionUrl, int numResults, List<User> userAccessList) {
        this.url = url;
        this.submissionUrl = submissionUrl;
        this.numResults = numResults;
        this.userAccessList = userAccessList;
    }

    public IDCollectionPointInfoResponse() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public List<User> getUserAccessList() {
        return userAccessList;
    }

    public void setUserAccessList(List<User> userAccessList) {
        this.userAccessList = userAccessList;
    }

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }
}
