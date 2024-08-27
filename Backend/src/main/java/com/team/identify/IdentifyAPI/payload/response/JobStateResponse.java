package com.team.identify.IdentifyAPI.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JobStateResponse {
    @Schema(
            nullable=true,
            description = "Contains the failure reason if the job failed due to processing errors"
    )
    String errorMessage;

    @Schema(
            description = "The current state of the job",
            anyOf = EJobState.class
    )
    String state;

    public JobStateResponse(EJobState state) {
        this.state = state.toString();
    }

    public JobStateResponse(EJobState state, String errorMessage) {
        this.errorMessage = errorMessage;
        this.state = state.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
