package com.alphasteel.alphaapi.domain.dto;

/** Unit of measure create request. */
public record UnitOfMeasureCreateRequest(
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(max = 10)
    String code,
    
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(max = 50)
    String name
) {}
