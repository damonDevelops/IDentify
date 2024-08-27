package com.team.identify.IdentifyAPI.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Information about the currently authenticated user")
public class UserInfoResponse {
    @Schema(description = "Username")
    private String userName;
    @Schema(description = "Email address")
    private String email;
    @Schema(description = "The user's ID")
    private Long userId;
    @Schema(description = "A list of companies the user is a member of")
    private List<CompanyResponse> companyList;

    public UserInfoResponse() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCompanyList(List<CompanyResponse> companyList) {
        this.companyList = companyList;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    public List<CompanyResponse> getCompanyList() {
        return companyList;
    }
}
