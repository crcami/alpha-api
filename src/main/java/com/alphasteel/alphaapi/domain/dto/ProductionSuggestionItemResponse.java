package com.alphasteel.alphaapi.domain.dto;

/** Production suggestion item response. */
public record ProductionSuggestionItemResponse(
    Long productId,
    String productCode,
    String productName,
    java.math.BigDecimal unitValue,
    long quantity,
    java.math.BigDecimal totalValue,
    String unitOfMeasure
) {}
