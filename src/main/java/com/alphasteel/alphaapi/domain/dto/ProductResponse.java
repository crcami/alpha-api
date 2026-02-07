package com.alphasteel.alphaapi.domain.dto;

import java.math.BigDecimal;

/** Product response payload. */
public record ProductResponse(
    Long id,
    String code,
    String name,
    BigDecimal value
) {}
