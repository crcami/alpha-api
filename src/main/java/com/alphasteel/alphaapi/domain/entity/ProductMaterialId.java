package com.alphasteel.alphaapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/** Composite key for product materials. */
@Embeddable
public class ProductMaterialId implements Serializable {

  @Column(name = "PRODUCT_ID", nullable = false)
  public Long productId;

  @Column(name = "RAW_MATERIAL_ID", nullable = false)
  public Long rawMaterialId;

  public ProductMaterialId() {}

  public ProductMaterialId(Long productId, Long rawMaterialId) {
    this.productId = productId;
    this.rawMaterialId = rawMaterialId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProductMaterialId that)) {
      return false;
    }
    return Objects.equals(productId, that.productId)
        && Objects.equals(rawMaterialId, that.rawMaterialId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, rawMaterialId);
  }
}
