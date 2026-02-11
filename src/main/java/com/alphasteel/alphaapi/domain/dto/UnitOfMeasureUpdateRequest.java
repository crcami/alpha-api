package com.alphasteel.alphaapi.domain.dto;

/** Unit of measure update request. */
public record UnitOfMeasureUpdateRequest(
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(max = 10)
    String code,
    
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(max = 50)
    String name
) {}
