package com.alphasteel.alphaapi.common;

import java.time.Instant;

/** Standard API error payload. */
public record ApiError(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
) {}
