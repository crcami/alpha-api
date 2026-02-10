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

@Entity
@Table(name = "product_material")
public class ProductMaterialEntity extends PanacheEntityBase {

  @EmbeddedId
  public ProductMaterialId id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("productId")
  @JoinColumn(name = "product_id", nullable = false)
  public ProductEntity product;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("rawMaterialId")
  @JoinColumn(name = "raw_material_id", nullable = false)
  public RawMaterialEntity rawMaterial;

  @Column(name = "quantity_required", nullable = false, precision = 18, scale = 3)
  public BigDecimal quantityRequired;
}
