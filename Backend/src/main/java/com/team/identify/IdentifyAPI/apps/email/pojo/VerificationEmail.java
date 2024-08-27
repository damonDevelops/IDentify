package com.team.identify.IdentifyAPI.apps.email.pojo;

import com.team.identify.IdentifyAPI.model.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;

public class VerificationEmail extends EmailMessage {
    private String templateName;
    private String sendFrom;

    private final String baseUrl =
            ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

    private final String loginUrl = baseUrl + "/login";

    private final String fullName;

    private final String verificationLink;

    private final String emailAddress;

    public VerificationEmail(User user, String verificationLink) {
        super(user.getEmail(), "Welcome to IDentify - Verify your email to get started");
        this.fullName = user.getFullName();
        this.verificationLink = baseUrl.concat(verificationLink);
        this.emailAddress = user.getEmail();
        this.sendFrom = "support@identify.rodeo";
        this.templateName = "email/signup.html";
    }

    @Override
    public Context getContext() {
        Context _c = new Context();
        _c.setVariable("fullName", fullName);
        _c.setVariable("verificationLink", verificationLink);
        _c.setVariable("emailAddress", emailAddress);
        _c.setVariable("loginUrl", loginUrl);
        _c.setVariable("fullName", fullName);
        return _c;
    }

    @Override
    public String getTemplateName() {
        return this.templateName;
    }

    @Override
    public String getSendTo() {
        return this.sendTo;
    }

    @Override
    public String getCc() {
        return this.cc;
    }

    @Override
    public String getBcc() {
        return this.bcc;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public String getSendFrom() {
        return this.sendFrom;
    }
}
