package com.alphasteel.alphaapi.domain.dto;

/** Raw material create request. */
public record RawMaterialCreateRequest(
    String code,
    @jakarta.validation.constraints.NotBlank String name,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive java.math.BigDecimal stockQuantity,
    String unitOfMeasure) {}
