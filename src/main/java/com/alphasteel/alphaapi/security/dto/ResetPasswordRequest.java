package com.alphasteel.alphaapi.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Reset password request. */
public record ResetPasswordRequest(
    @NotBlank String token,
    @NotBlank @Size(min = 8, max = 72) String newPassword
) {}
