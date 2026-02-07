package com.alphasteel.alphaapi.common;

import java.text.Normalizer;

/** Normalizes text for code generation. */
public final class TextNormalizer {

  private TextNormalizer() {}

  public static String toAsciiUpper(String value) {
    if (value == null) {
      return "";
    }

    String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
    String withoutMarks = normalized.replaceAll("\\p{M}+", "");
    return withoutMarks.toUpperCase();
  }

  public static String lettersOnly(String value) {
    if (value == null) {
      return "";
    }
    return value.replaceAll("[^A-Z]", "");
  }
}
