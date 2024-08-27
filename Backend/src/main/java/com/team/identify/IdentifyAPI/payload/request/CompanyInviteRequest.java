package com.team.identify.IdentifyAPI.payload.request;

import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import com.team.identify.IdentifyAPI.model.validators.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for company invites")
public class CompanyInviteRequest {
    @Schema(description = "The user's email address.")
    @NotBlank
    @Email
    String email;
    @Schema(description = "The role the user will have in the company")
    @ValueOfEnum(enumClass = ECompanyRole.class)
    String role;

    public CompanyInviteRequest() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
