package com.alphasteel.alphaapi.domain.dto;

import java.math.BigDecimal;

/** Raw material response payload. */
public record RawMaterialResponse(
    Long id,
    String code,
    String name,
    BigDecimal stockQuantity
) {}
