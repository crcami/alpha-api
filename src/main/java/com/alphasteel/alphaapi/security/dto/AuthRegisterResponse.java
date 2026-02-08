package com.alphasteel.alphaapi.security.dto;

/** Registration response payload. */
public record AuthRegisterResponse(
    long id,
    String name,
    String email
) {}
