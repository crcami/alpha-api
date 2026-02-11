package com.alphasteel.alphaapi.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum UnitOfMeasure {
  UN,
  KG,
  G,
  L,
  ML;

  @JsonCreator
  public static UnitOfMeasure fromString(String value) {
    if (value == null) return null;
    return Stream.of(UnitOfMeasure.values())
        .filter(u -> u.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid unit of measure: " + value));
  }
}
