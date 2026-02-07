package com.alphasteel.alphaapi.domain.dto;

import java.math.BigDecimal;

/** BOM item response payload. */
public record ProductBomItemResponse(
    Long rawMaterialId,
    String rawMaterialCode,
    String rawMaterialName,
    BigDecimal quantityRequired
) {}
