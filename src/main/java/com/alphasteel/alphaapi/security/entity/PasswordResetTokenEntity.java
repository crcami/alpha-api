package com.alphasteel.alphaapi.security.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;

/** Password reset token entity. */
@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetTokenEntity extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      name = "SEQ_PASSWORD_RESET_TOKEN",
      sequenceName = "SEQ_PASSWORD_RESET_TOKEN",
      allocationSize = 1
  )
  @GeneratedValue(generator = "SEQ_PASSWORD_RESET_TOKEN")
  public Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  public AppUserEntity user;

  @Column(name = "TOKEN_HASH", nullable = false, length = 64)
  public String tokenHash;

  @Column(name = "EXPIRES_AT", nullable = false)
  public Instant expiresAt;

  @Column(name = "USED_AT")
  public Instant usedAt;
}
