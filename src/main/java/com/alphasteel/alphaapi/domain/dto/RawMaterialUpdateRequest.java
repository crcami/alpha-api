package com.alphasteel.alphaapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/** Raw material update request. */
public record RawMaterialUpdateRequest(
    String code,
    @NotBlank String name,
    @NotNull @PositiveOrZero BigDecimal stockQuantity
) {}
