package com.team.identify.IdentifyAPI.apps.email.service;

import com.team.identify.IdentifyAPI.apps.email.pojo.VerificationEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final SpringTemplateEngine emailTemplateEngine;
    private final JavaMailSender javaMailSender;

    public EmailService(
            SpringTemplateEngine emailTemplateEngine,
            JavaMailSender javaMailSender) {
        this.emailTemplateEngine = emailTemplateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(VerificationEmail email) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(email.getSendTo());
        helper.setFrom(email.getSendFrom());
        helper.setSubject(email.getSubject());
        String html = emailTemplateEngine.process(email.getTemplateName(), email.getContext());
        helper.setText(html, true);
        javaMailSender.send(message);
    }
}
