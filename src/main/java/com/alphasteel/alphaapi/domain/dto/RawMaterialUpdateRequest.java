package com.alphasteel.alphaapi.domain.dto;

import com.alphasteel.alphaapi.domain.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/** Raw material update request. */
public record RawMaterialUpdateRequest(
    String code,
    @NotBlank(message = "update.request.name.must.not.be.blank") String name,
    @NotNull(message = "update.request.stockQuantity.must.not.be.null")
        @PositiveOrZero(message = "update.request.stockQuantity.must.be.positive.or.zero")
        BigDecimal stockQuantity,
    UnitOfMeasure unitOfMeasure) {}
