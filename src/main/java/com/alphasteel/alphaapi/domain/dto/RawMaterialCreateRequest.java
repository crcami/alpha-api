package com.alphasteel.alphaapi.domain.dto;

import com.alphasteel.alphaapi.domain.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record RawMaterialCreateRequest(
    String code,
    @NotBlank(message = "create.request.name.must.not.be.blank") String name,
    @NotNull(message = "create.request.stockQuantity.must.not.be.null")
        @PositiveOrZero(message = "create.request.stockQuantity.must.be.positive.or.zero")
        BigDecimal stockQuantity,
    UnitOfMeasure unitOfMeasure) {}
