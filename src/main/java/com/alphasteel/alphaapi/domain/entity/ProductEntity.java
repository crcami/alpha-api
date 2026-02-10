package com.alphasteel.alphaapi.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/** Product entity. */
@Entity
@Table(
    name = "product",
    uniqueConstraints = @UniqueConstraint(name = "uk_product_code", columnNames = "code")
)
public class ProductEntity extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "seq_product", sequenceName = "seq_product", allocationSize = 1)
  @GeneratedValue(generator = "seq_product")
  public Long id;

  @NotBlank
  @Column(name = "code", nullable = false, length = 32)
  public String code;

  @NotBlank
  @Column(name = "name", nullable = false, length = 255)
  public String name;

  @Column(name = "value", nullable = false, precision = 15, scale = 2)
  public BigDecimal value;
}
