package com.team.identify.IdentifyAPI.payload.request;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {return this.refreshToken;}

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    
}
