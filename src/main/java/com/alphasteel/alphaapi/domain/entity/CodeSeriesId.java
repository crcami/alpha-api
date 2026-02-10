package com.alphasteel.alphaapi.domain.entity;

import com.alphasteel.alphaapi.domain.CodeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CodeSeriesId implements Serializable {

  @Enumerated(EnumType.STRING)
  @Column(name = "code_type", nullable = false, length = 32)
  public CodeType codeType;

  @Column(name = "prefix", nullable = false, length = 8)
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
