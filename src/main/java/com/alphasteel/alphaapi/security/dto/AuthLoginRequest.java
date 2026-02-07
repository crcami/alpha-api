package com.alphasteel.alphaapi.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Login request. */
public record AuthLoginRequest(
    @Email @NotBlank String email,
    @NotBlank String password
) {}
