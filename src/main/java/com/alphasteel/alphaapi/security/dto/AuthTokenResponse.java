package com.alphasteel.alphaapi.security.dto;

/** Token response with refresh token support. */
public record AuthTokenResponse(
    String tokenType,
    String accessToken,
    long expiresInSeconds,
    String refreshToken
) {}
