package com.alphasteel.alphaapi.security.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/** Refresh token entity for token rotation. */
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(generator = "seq_refresh_token")
  @jakarta.persistence.SequenceGenerator(
      name = "seq_refresh_token",
      sequenceName = "seq_refresh_token",
      allocationSize = 1
  )
  public Long id;

  @Column(name = "token", nullable = false, unique = true, length = 255)
  public String token;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  public AppUserEntity user;

  @Column(name = "expires_at", nullable = false)
  public LocalDateTime expiresAt;

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt;
}
