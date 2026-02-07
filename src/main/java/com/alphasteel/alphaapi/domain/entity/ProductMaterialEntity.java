package com.alphasteel.alphaapi.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/** Product bill of materials item. */
@Entity
@Table(name = "PRODUCT_MATERIAL")
public class ProductMaterialEntity extends PanacheEntityBase {

  @EmbeddedId
  public ProductMaterialId id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("productId")
  @JoinColumn(name = "PRODUCT_ID", nullable = false)
  public ProductEntity product;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("rawMaterialId")
  @JoinColumn(name = "RAW_MATERIAL_ID", nullable = false)
  public RawMaterialEntity rawMaterial;

  @Column(name = "QTY_REQUIRED", nullable = false, precision = 18, scale = 3)
  public BigDecimal quantityRequired;
}
