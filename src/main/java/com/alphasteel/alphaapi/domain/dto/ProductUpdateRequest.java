package com.alphasteel.alphaapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/** Product update request. */
public record ProductUpdateRequest(
    String code,
    @NotBlank String name,
    @NotNull @Positive BigDecimal value
) {}
