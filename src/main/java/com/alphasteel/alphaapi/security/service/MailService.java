package com.alphasteel.alphaapi.security.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** Sends application emails. */
@ApplicationScoped
public class MailService {

  @Inject
  Mailer mailer;

  public void sendPasswordResetEmail(String toEmail, String resetLink) {
    Mail mail = Mail.withText(
        toEmail,
        "Password reset",
        "Use this link to reset your password: " + resetLink
    );
    mailer.send(mail);
  }
}
