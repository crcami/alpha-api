package com.alphasteel.alphaapi.domain.dto;

/** Product create request. */
public record ProductCreateRequest(
    String code,
    @jakarta.validation.constraints.NotBlank String name,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive java.math.BigDecimal value,
    String unitOfMeasure
) {}
