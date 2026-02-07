package com.alphasteel.alphaapi.common;

import com.alphasteel.alphaapi.domain.CodeType;
import com.alphasteel.alphaapi.repository.CodeSeriesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/** Generates unique codes for entities. */
@ApplicationScoped
public class CodeGenerator {

  @Inject
  CodeSeriesRepository codeSeriesRepository;

  @Transactional
  public String generateCode(CodeType type, String name) {
    String prefix = buildPrefix(name);
    long next = codeSeriesRepository.nextFor(type, prefix);
    return prefix + String.format("%05d", next);
  }

  private String buildPrefix(String name) {
    String upper = TextNormalizer.toAsciiUpper(name);
    String letters = TextNormalizer.lettersOnly(upper);

    if (letters.length() >= 2) {
      return letters.substring(0, 2);
    }
    if (letters.length() == 1) {
      return letters + "X";
    }
    return "XX";
  }
}
