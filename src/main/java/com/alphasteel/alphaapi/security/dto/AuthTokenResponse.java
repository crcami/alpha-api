package com.alphasteel.alphaapi.security.dto;

/** Token response payload. */
public record AuthTokenResponse(
    String tokenType,
    String accessToken,
    long expiresInSeconds
) {}
