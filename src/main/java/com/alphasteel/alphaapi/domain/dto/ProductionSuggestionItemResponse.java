package com.alphasteel.alphaapi.domain.dto;

import java.math.BigDecimal;

/** Production suggestion item response. */
public record ProductionSuggestionItemResponse(
    Long productId,
    String productCode,
    String productName,
    BigDecimal unitValue,
    long quantity,
    BigDecimal totalValue
) {}
