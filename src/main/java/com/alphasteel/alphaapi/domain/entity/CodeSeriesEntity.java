package com.alphasteel.alphaapi.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "code_series")
public class CodeSeriesEntity extends PanacheEntityBase {

  @EmbeddedId
  public CodeSeriesId id;

  @Column(name = "next_number", nullable = false)
  public long nextNumber;
}
