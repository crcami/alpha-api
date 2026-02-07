package com.alphasteel.alphaapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/** Product create request. */
public record ProductCreateRequest(
    String code,
    @NotBlank String name,
    @NotNull @Positive BigDecimal value
) {}
