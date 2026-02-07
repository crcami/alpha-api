package com.alphasteel.alphaapi.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Forgot password request. */
public record ForgotPasswordRequest(
    @Email @NotBlank String email
) {}
