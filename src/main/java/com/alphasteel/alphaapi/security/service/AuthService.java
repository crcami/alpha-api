package com.alphasteel.alphaapi.security.service;

import com.alphasteel.alphaapi.exception.ConflictException;
import com.alphasteel.alphaapi.exception.UnauthorizedException;
import com.alphasteel.alphaapi.security.entity.AppUserEntity;
import com.alphasteel.alphaapi.security.repository.AppUserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Set;

/** Handles auth operations. */
@ApplicationScoped
public class AuthService {

  @Inject
  AppUserRepository appUserRepository;

  @Inject
  JwtService jwtService;

  @Transactional
  public void register(String email, String password) {
    String normalizedEmail = email.trim().toLowerCase();

    if (appUserRepository.findByEmail(normalizedEmail).isPresent()) {
      throw new ConflictException("Email already registered.");
    }

    AppUserEntity user = new AppUserEntity();
    user.email = normalizedEmail;
    user.passwordHash = BcryptUtil.bcryptHash(password);
    user.roles = "USER";

    appUserRepository.persist(user);
  }

  public String login(String email, String password) {
    String normalizedEmail = email.trim().toLowerCase();

    AppUserEntity user = appUserRepository.findByEmail(normalizedEmail)
        .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

    if (!BcryptUtil.matches(password, user.passwordHash)) {
      throw new UnauthorizedException("Invalid credentials.");
    }

    Set<String> roles = Set.of(user.roles.split(","));
    return jwtService.issueToken(user.email, roles);
  }
}
