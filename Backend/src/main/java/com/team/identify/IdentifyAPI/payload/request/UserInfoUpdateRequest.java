package com.team.identify.IdentifyAPI.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO used for user submitted details about their account")
public class UserInfoUpdateRequest {
    @Schema(description = "User's new email address")
    private String newEmail;
    @Schema(description = "User's current password, required for encryption")
    private String oldPassword;
    @Schema(description = "User's new password")
    private String newPassword;

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
