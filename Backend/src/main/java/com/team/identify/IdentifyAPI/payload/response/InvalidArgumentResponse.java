package com.team.identify.IdentifyAPI.payload.response;

import java.util.List;
import java.util.Map;

public class InvalidArgumentResponse {
    String message;
    Map<String, List<String>> details;

    public InvalidArgumentResponse(String message, Map<String, List<String>> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getDetails() {
        return details;
    }

    public void setDetails(Map<String, List<String>> details) {
        this.details = details;
    }
}
