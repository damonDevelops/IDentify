package com.team.identify.IdentifyAPI.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO used when a user is requesting encrypted data from the API")
public class EncryptedResourceRequest {
    @Schema(description = "The user's plaintext password")
    @NotBlank
    String password;
    @Schema(description = "The page number (paginated requests)")
    Integer pageNumber;

    public EncryptedResourceRequest() {
    }

    public EncryptedResourceRequest(String password) {
        this.password = password;
    }


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
