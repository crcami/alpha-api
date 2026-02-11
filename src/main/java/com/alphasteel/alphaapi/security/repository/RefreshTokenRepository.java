package com.alphasteel.alphaapi.security.repository;

import com.alphasteel.alphaapi.security.entity.RefreshTokenEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Optional;

/** Repository for refresh tokens. */
@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshTokenEntity> {

  public Optional<RefreshTokenEntity> findByToken(String token) {
    return find("token", token).firstResultOptional();
  }

  public long deleteByUserId(Long userId) {
    return delete("user.id", userId);
  }

  public long deleteExpired() {
    return delete("expiresAt < ?1", LocalDateTime.now());
  }
}
