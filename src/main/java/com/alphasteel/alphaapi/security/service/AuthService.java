package com.alphasteel.alphaapi.security.service;

import com.alphasteel.alphaapi.exception.ConflictException;
import com.alphasteel.alphaapi.exception.UnauthorizedException;
import com.alphasteel.alphaapi.security.dto.AuthTokenResponse;
import com.alphasteel.alphaapi.security.entity.AppUserEntity;
import com.alphasteel.alphaapi.security.entity.RefreshTokenEntity;
import com.alphasteel.alphaapi.security.repository.AppUserRepository;
import com.alphasteel.alphaapi.security.repository.RefreshTokenRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/** Handles auth operations. */
@ApplicationScoped
public class AuthService {

  @Inject
  AppUserRepository appUserRepository;

  @Inject
  RefreshTokenRepository refreshTokenRepository;

  @Inject
  JwtService jwtService;

  @ConfigProperty(name = "app.security.refresh-token-ttl-minutes", defaultValue = "30")
  int refreshTokenTtlMinutes;

  @Transactional
  public AppUserEntity register(String name, String email, String password) {
    String normalizedEmail = email.trim().toLowerCase();
    String normalizedName = name.trim();

    if (appUserRepository.findByEmail(normalizedEmail).isPresent()) {
      throw new ConflictException("Email already registered.");
    }

    AppUserEntity user = new AppUserEntity();
    user.name = normalizedName;
    user.email = normalizedEmail;
    user.passwordHash = BcryptUtil.bcryptHash(password);
    user.roles = "USER";

    appUserRepository.persist(user);
    return user;
  }

  @Transactional
  public AuthTokenResponse login(String email, String password) {
    String normalizedEmail = email.trim().toLowerCase();

    AppUserEntity user = appUserRepository.findByEmail(normalizedEmail)
        .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

    if (!BcryptUtil.matches(password, user.passwordHash)) {
      throw new UnauthorizedException("Invalid credentials.");
    }

    // Delete old refresh tokens for this user
    refreshTokenRepository.deleteByUserId(user.id);

    // Generate new tokens
    Set<String> roles = Set.of(user.roles.split(","));
    String accessToken = jwtService.issueToken(user.email, roles);
    String refreshToken = generateRefreshToken(user);

    return new AuthTokenResponse("Bearer", accessToken, 600, refreshToken);
  }

  @Transactional
  public AuthTokenResponse refresh(String refreshTokenValue) {
    // Find and validate refresh token
    RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
        .orElseThrow(() -> new UnauthorizedException("Invalid refresh token."));

    // Check if expired
    if (refreshToken.expiresAt.isBefore(LocalDateTime.now())) {
      refreshTokenRepository.delete(refreshToken);
      throw new UnauthorizedException("Refresh token expired.");
    }

    // Delete old refresh token (rotating tokens for security)
    refreshTokenRepository.delete(refreshToken);

    // Generate new tokens
    AppUserEntity user = refreshToken.user;
    Set<String> roles = Set.of(user.roles.split(","));
    String accessToken = jwtService.issueToken(user.email, roles);
    String newRefreshToken = generateRefreshToken(user);

    return new AuthTokenResponse("Bearer", accessToken, 600, newRefreshToken);
  }

  private String generateRefreshToken(AppUserEntity user) {
    String tokenValue = UUID.randomUUID().toString();

    RefreshTokenEntity refreshToken = new RefreshTokenEntity();
    refreshToken.token = tokenValue;
    refreshToken.user = user;
    refreshToken.createdAt = LocalDateTime.now();
    refreshToken.expiresAt = LocalDateTime.now().plusMinutes(refreshTokenTtlMinutes);

    refreshTokenRepository.persist(refreshToken);
    return tokenValue;
  }
}
