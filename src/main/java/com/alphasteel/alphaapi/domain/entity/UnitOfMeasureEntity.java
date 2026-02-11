package com.alphasteel.alphaapi.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unit_of_measure")
public class UnitOfMeasureEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(generator = "seq_unit_of_measure")
  @jakarta.persistence.SequenceGenerator(
      name = "seq_unit_of_measure",
      sequenceName = "seq_unit_of_measure",
      allocationSize = 1
  )
  public Long id;

  @Column(name = "code", nullable = false, unique = true, length = 10)
  public String code;

  @Column(name = "name", nullable = false, length = 50)
  public String name;
}
