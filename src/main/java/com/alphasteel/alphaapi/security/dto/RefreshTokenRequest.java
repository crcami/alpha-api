package com.alphasteel.alphaapi.security.dto;

import jakarta.validation.constraints.NotBlank;

/** Request to refresh access token. */
public record RefreshTokenRequest(
    @NotBlank String refreshToken
) {}
