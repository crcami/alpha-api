package com.alphasteel.alphaapi.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/** BOM upsert item request. */
public record ProductMaterialUpsertRequest(
    @NotNull Long rawMaterialId,
    @NotNull @Positive BigDecimal quantityRequired
) {}
