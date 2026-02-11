package com.alphasteel.alphaapi.security.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.Duration;

/** Sends application emails. */
@ApplicationScoped
public class MailService {

  private static final Logger LOG = Logger.getLogger(MailService.class);
  private static final String SUBJECT = "Recuperação de Senha - Alpha Steel";

  @ConfigProperty(name = "app.email.resend.api-key", defaultValue = "")
  String resendApiKey;

  @ConfigProperty(name = "app.email.resend.from", defaultValue = "")
  String resendFrom;

  @Inject
  Vertx vertx;

  @Inject
  ResendClient resendClient;

  @Inject
  ReactiveMailer mailer;

  public void sendPasswordResetEmailAsync(String toEmail, String resetLink) {
    String safeLink = escapeHtmlAttribute(resetLink);
    String htmlContent = buildPasswordResetEmailHtml(safeLink);

    if (!isBlank(resendApiKey) && !isBlank(resendFrom)) {
      sendViaResendAsync(toEmail, htmlContent);
      return;
    }

    sendViaSmtpAsync(toEmail, htmlContent);
  }

  private void sendViaResendAsync(String toEmail, String htmlContent) {
    vertx.executeBlocking(() -> {
      try {
        String id = resendClient.sendHtmlEmail(
            resendApiKey,
            resendFrom,
            toEmail,
            SUBJECT,
            htmlContent
        );
        LOG.infof("Password reset email sent via Resend. id=%s to=%s", id, toEmail);
        return null; // Callable requires a return value
      } catch (Exception e) {
        LOG.errorf(e, "Failed to send password reset email via Resend to %s", toEmail);
        throw e; // Rethrow to fail the blocking action
      }
    }, false);
  }

  private void sendViaSmtpAsync(String toEmail, String htmlContent) {
    Mail mailObj = Mail.withHtml(toEmail, SUBJECT, htmlContent);

    mailer.send(mailObj)
        .ifNoItem().after(Duration.ofSeconds(60)).fail()
        .subscribe().with(
            ignored -> LOG.infof("Password reset email sent via SMTP to %s", toEmail),
            err -> LOG.errorf(err, "Failed to send password reset email via SMTP to %s", toEmail)
        );
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private String escapeHtmlAttribute(String value) {
    if (value == null) {
      return "";
    }
    return value
        .replace("&", "&amp;")
        .replace("\"", "&quot;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("'", "&#39;");
  }

  private String buildPasswordResetEmailHtml(String resetLink) {
    return """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
            <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                <tr>
                    <td align="center" style="padding: 40px 0;">
                        <table role="presentation" style="width: 600px; border-collapse: collapse; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                            <tr>
                                <td style="padding: 40px 40px 30px 40px; text-align: center; background-color: #6972ab; border-radius: 8px 8px 0 0;">
                                    <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: bold;">Alpha Steel</h1>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 40px;">
                                    <h2 style="margin: 0 0 20px 0; color: #333333; font-size: 24px;">Recuperação de Senha</h2>
                                    <p style="margin: 0 0 20px 0; color: #666666; font-size: 16px; line-height: 24px;">
                                        Você solicitou a recuperação de senha da sua conta Alpha Steel.
                                    </p>
                                    <p style="margin: 0 0 30px 0; color: #666666; font-size: 16px; line-height: 24px;">
                                        Clique no botão abaixo para criar uma nova senha:
                                    </p>
                                    <table role="presentation" style="margin: 0 auto;">
                                        <tr>
                                            <td style="border-radius: 5px; background-color: #6972ab;">
                                                <a href="%s" target="_blank" style="display: inline-block; padding: 14px 32px; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                                    Redefinir Senha
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
                                    <p style="margin: 30px 0 0 0; color: #999999; font-size: 14px; line-height: 20px;">
                                        ⏱️ <strong>Este link expira em 30 minutos.</strong>
                                    </p>
                                    <p style="margin: 10px 0 0 0; color: #999999; font-size: 14px; line-height: 20px;">
                                        Se você não solicitou esta recuperação de senha, por favor ignore este email. Sua senha permanecerá inalterada.
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 30px 40px; background-color: #f8f8f8; border-radius: 0 0 8px 8px; text-align: center;">
                                    <p style="margin: 0; color: #999999; font-size: 12px;">
                                        © 2026 Alpha Steel. Todos os direitos reservados.
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """.formatted(resetLink);
  }
}
