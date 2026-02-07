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
    name = "PRODUCT",
    uniqueConstraints = @UniqueConstraint(name = "UK_PRODUCT_CODE", columnNames = "CODE")
)
public class ProductEntity extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "SEQ_PRODUCT", sequenceName = "SEQ_PRODUCT", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_PRODUCT")
  public Long id;

  @NotBlank
  @Column(name = "CODE", nullable = false, length = 32)
  public String code;

  @NotBlank
  @Column(name = "NAME", nullable = false, length = 255)
  public String name;

  @Column(name = "VALUE", nullable = false, precision = 15, scale = 2)
  public BigDecimal value;
}
