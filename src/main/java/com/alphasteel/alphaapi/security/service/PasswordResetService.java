package com.alphasteel.alphaapi.security.service;

import com.alphasteel.alphaapi.exception.ConflictException;
import com.alphasteel.alphaapi.security.entity.AppUserEntity;
import com.alphasteel.alphaapi.security.entity.PasswordResetTokenEntity;
import com.alphasteel.alphaapi.security.repository.AppUserRepository;
import com.alphasteel.alphaapi.security.repository.PasswordResetTokenRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/** Handles password reset flow. */
@ApplicationScoped
public class PasswordResetService {

  @Inject
  AppUserRepository appUserRepository;

  @Inject
  PasswordResetTokenRepository tokenRepository;

  @Inject
  TokenGenerator tokenGenerator;

  @Inject
  TokenHasher tokenHasher;

  @Inject
  MailService mailService;

  @ConfigProperty(name = "app.frontend.base-url")
  String frontendBaseUrl;

  @ConfigProperty(name = "app.security.reset-token-ttl-minutes", defaultValue = "30")
  long tokenTtlMinutes;

  @Transactional
  public void requestReset(String email) {
    String normalizedEmail = email.trim().toLowerCase();
    AppUserEntity user = appUserRepository.findByEmail(normalizedEmail).orElse(null);

    if (user == null) {
      return;
    }

    String token = tokenGenerator.generateUrlSafeToken();
    String tokenHash = tokenHasher.sha256Hex(token);

    PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
    entity.user = user;
    entity.tokenHash = tokenHash;
    entity.expiresAt = Instant.now().plus(tokenTtlMinutes, ChronoUnit.MINUTES);

    tokenRepository.persist(entity);

    String link = frontendBaseUrl + "/reset-password?token=" + token;
    mailService.sendPasswordResetEmail(user.email, link);
  }

  @Transactional
  public void resetPassword(String token, String newPassword) {
    String tokenHash = tokenHasher.sha256Hex(token);
    Instant now = Instant.now();

    PasswordResetTokenEntity entity = tokenRepository.findActive(tokenHash, now)
        .orElseThrow(() -> new ConflictException("Invalid or expired token."));

    entity.user.passwordHash = BcryptUtil.bcryptHash(newPassword);
    entity.usedAt = now;
  }
}
