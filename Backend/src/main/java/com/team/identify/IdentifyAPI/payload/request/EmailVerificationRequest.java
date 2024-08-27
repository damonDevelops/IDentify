package com.team.identify.IdentifyAPI.payload.request;

public class EmailVerificationRequest {
    private String password;

    private String email;

    private String verificationToken;

    public EmailVerificationRequest(String password, String email, String verificationToken) {
        this.password = password;
        this.email = email;
        this.verificationToken = verificationToken;
    }

    public EmailVerificationRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
