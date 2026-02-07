package com.alphasteel.alphaapi.security.repository;

import com.alphasteel.alphaapi.security.entity.PasswordResetTokenEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;

/** Password reset token repository. */
@ApplicationScoped
public class PasswordResetTokenRepository
    implements PanacheRepositoryBase<PasswordResetTokenEntity, Long> {

  public Optional<PasswordResetTokenEntity> findActive(String tokenHash, Instant now) {
    return find("tokenHash = ?1 and usedAt is null and expiresAt > ?2", tokenHash, now)
        .firstResultOptional();
  }
}
