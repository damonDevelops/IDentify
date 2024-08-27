package com.team.identify.IdentifyAPI.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Basic message response")
public class MessageResponse {
    @Schema(description = "Contains message")
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
