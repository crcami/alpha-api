package com.alphasteel.alphaapi.domain.dto;

/** Product update request. */
public record ProductUpdateRequest(
    String code,
    @jakarta.validation.constraints.NotBlank String name,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive java.math.BigDecimal value,
    String unitOfMeasure
) {}
