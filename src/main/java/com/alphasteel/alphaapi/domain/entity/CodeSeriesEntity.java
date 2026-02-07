package com.alphasteel.alphaapi.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/** Stores code sequence by prefix/type. */
@Entity
@Table(name = "CODE_SERIES")
public class CodeSeriesEntity extends PanacheEntityBase {

  @EmbeddedId
  public CodeSeriesId id;

  @Column(name = "NEXT_NUMBER", nullable = false)
  public long nextNumber;
}
