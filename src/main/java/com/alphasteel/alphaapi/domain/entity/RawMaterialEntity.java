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

/** Raw material entity. */
@Entity
@Table(
    name = "RAW_MATERIAL",
    uniqueConstraints = @UniqueConstraint(name = "UK_RAW_MATERIAL_CODE", columnNames = "CODE")
)
public class RawMaterialEntity extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "SEQ_RAW_MATERIAL", sequenceName = "SEQ_RAW_MATERIAL", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_RAW_MATERIAL")
  public Long id;

  @NotBlank
  @Column(name = "CODE", nullable = false, length = 32)
  public String code;

  @NotBlank
  @Column(name = "NAME", nullable = false, length = 255)
  public String name;

  @Column(name = "STOCK_QTY", nullable = false, precision = 18, scale = 3)
  public BigDecimal stockQuantity;
}
