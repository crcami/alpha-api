package com.alphasteel.alphaapi.domain.entity;

import com.alphasteel.alphaapi.domain.UnitOfMeasure;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "raw_material")
public class RawMaterialEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  public String code;

  @Column(name = "name", nullable = false, length = 255)
  public String name;

  @Column(name = "stock_qty", nullable = false, precision = 18, scale = 3)
  public BigDecimal stockQty;

  @Enumerated(EnumType.STRING)
  @Column(name = "unit_of_measure", nullable = false, length = 8)
  @ColumnDefault("'UN'")
  public UnitOfMeasure unitOfMeasure = UnitOfMeasure.UN;
}
