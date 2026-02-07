package com.alphasteel.alphaapi.security.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/** Application user entity. */
@Entity
@UserDefinition
@Table(
    name = "APP_USER",
    uniqueConstraints = @UniqueConstraint(name = "UK_APP_USER_EMAIL", columnNames = "EMAIL")
)
public class AppUserEntity extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "SEQ_APP_USER", sequenceName = "SEQ_APP_USER", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_APP_USER")
  public Long id;

  @Username
  @Column(name = "EMAIL", nullable = false, length = 320)
  public String email;

  @Password
  @Column(name = "PASSWORD_HASH", nullable = false, length = 200)
  public String passwordHash;

  @Roles
  @Column(name = "ROLES", nullable = false, length = 200)
  public String roles;
}
