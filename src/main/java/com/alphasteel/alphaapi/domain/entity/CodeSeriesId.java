package com.alphasteel.alphaapi.domain.entity;

import com.alphasteel.alphaapi.domain.CodeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

/** Composite key for code series. */
@Embeddable
public class CodeSeriesId implements Serializable {

  @Enumerated(EnumType.STRING)
  @Column(name = "CODE_TYPE", nullable = false, length = 32)
  public CodeType codeType;

  @Column(name = "PREFIX", nullable = false, length = 8)
  public String prefix;

  public CodeSeriesId() {}

  public CodeSeriesId(CodeType codeType, String prefix) {
    this.codeType = codeType;
    this.prefix = prefix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CodeSeriesId that)) {
      return false;
    }
    return codeType == that.codeType && Objects.equals(prefix, that.prefix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeType, prefix);
  }
}
