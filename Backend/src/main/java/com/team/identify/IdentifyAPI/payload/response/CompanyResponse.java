package com.team.identify.IdentifyAPI.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

// used for UserInfoResponse in UserController
@Schema(description = "DTO used when getting the user's companies in UserController")
public class CompanyResponse {
    @Schema(description = "The company name")
    private String companyName;
    @Schema(description = "The company ID")
    private UUID id;
    @Schema(description = "The unique endpoint for the company")
    private String companyEndpoint;
    @Schema(description = "The time the company was created")
    private Instant created;

    public CompanyResponse() {
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UUID getId() {
        return id;
    }

    public String getCompanyEndpoint() {return companyEndpoint;}

    public void setCompanyEndpoint(String companyEndpoint) {this.companyEndpoint = companyEndpoint;}

    public Instant getCreated() {return created;}

    public void setCreated(Instant created) {this.created = created;}
}
