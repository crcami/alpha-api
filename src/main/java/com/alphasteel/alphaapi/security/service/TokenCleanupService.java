package com.alphasteel.alphaapi.security.service;

import com.alphasteel.alphaapi.security.repository.RefreshTokenRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/** Scheduled service to clean up expired refresh tokens. */
@ApplicationScoped
public class TokenCleanupService {

  @Inject
  RefreshTokenRepository refreshTokenRepository;

  /**
   * Runs every hour to delete expired refresh tokens.
   */
  @Scheduled(every = "1h")
  @Transactional
  public void cleanupExpiredTokens() {
    long deleted = refreshTokenRepository.deleteExpired();
    if (deleted > 0) {
      System.out.println("Cleaned up " + deleted + " expired refresh token(s)");
    }
  }
}
