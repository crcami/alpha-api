package com.alphasteel.alphaapi.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "raw_material")
public class RawMaterialEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(generator = "seq_raw_material")
  @jakarta.persistence.SequenceGenerator(
      name = "seq_raw_material",
      sequenceName = "seq_raw_material",
      allocationSize = 1
  )
  public Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  public String code;

  @Column(name = "name", nullable = false, length = 255)
  public String name;

  @Column(name = "stock_qty", nullable = false, precision = 18, scale = 3)
  public BigDecimal stockQty;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uom_id")
  public UnitOfMeasureEntity unitOfMeasure;
}
