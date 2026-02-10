package com.alphasteel.alphaapi.domain.dto;

import com.alphasteel.alphaapi.domain.UnitOfMeasure;
import java.math.BigDecimal;

/** Raw material response. */
public record RawMaterialResponse(
    Long id,
    String code,
    String name,
    BigDecimal stockQuantity,
    UnitOfMeasure unitOfMeasure) {}
