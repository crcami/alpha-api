package com.alphasteel.alphaapi.domain.dto;

/** Product response payload. */
public record ProductResponse(
    Long id,
    String code,
    @jakarta.validation.constraints.NotBlank String name,
    java.math.BigDecimal value,
    String unitOfMeasure
) {}
