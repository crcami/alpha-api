package com.alphasteel.alphaapi.domain.dto;

/** Raw material response payload. */
public record RawMaterialResponse(
    Long id,
    String code,
    String name,
    java.math.BigDecimal stockQuantity,
    String unitOfMeasure
) {}
