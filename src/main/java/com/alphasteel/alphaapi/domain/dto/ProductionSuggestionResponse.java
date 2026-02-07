package com.alphasteel.alphaapi.domain.dto;

import java.math.BigDecimal;
import java.util.List;

/** Production suggestion response. */
public record ProductionSuggestionResponse(
    List<ProductionSuggestionItemResponse> items,
    BigDecimal totalValue
) {}
