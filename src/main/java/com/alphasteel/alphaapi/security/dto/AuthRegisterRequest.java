package com.alphasteel.alphaapi.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Registration request. */
public record AuthRegisterRequest(
    @NotBlank @Size(min = 2, max = 120) String name,
    @Email @NotBlank @Size(max = 320) String email,
    @NotBlank @Size(min = 8, max = 72) String password
) {}
