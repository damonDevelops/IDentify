package com.team.identify.IdentifyAPI.apps.email.pojo;

import org.thymeleaf.context.Context;

abstract class EmailMessage {
  protected String sendTo;
  protected String cc;
  protected String bcc;
  protected String subject;
  protected String body;
  protected String templateName;

  protected String sendFrom;

  public EmailMessage(String sendTo, String cc, String bcc, String subject) {
    this.sendTo = sendTo;
    this.cc = cc;
    this.bcc = bcc;
    this.subject = subject;
  }

  public EmailMessage(String sendTo, String subject) {
    this.sendTo = sendTo;
    this.subject = subject;
    this.sendFrom = "api@identify.rodeo";
    this.templateName = "needsoverride.html";
  }

  abstract public Context getContext();

  abstract public String getTemplateName();

  abstract public String getSendTo();

  abstract public String getCc();

  abstract public String getBcc();

  abstract public String getSubject();

  abstract public String getSendFrom();
}
