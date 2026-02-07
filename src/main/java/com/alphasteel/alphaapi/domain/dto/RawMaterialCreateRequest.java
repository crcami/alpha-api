package com.alphasteel.alphaapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/** Raw material create request. */
public record RawMaterialCreateRequest(
    String code,
    @NotBlank String name,
    @NotNull @PositiveOrZero BigDecimal stockQuantity
) {}
