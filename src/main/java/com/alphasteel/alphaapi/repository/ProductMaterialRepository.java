package com.alphasteel.alphaapi.repository;

import com.alphasteel.alphaapi.domain.entity.ProductMaterialEntity;
import com.alphasteel.alphaapi.domain.entity.ProductMaterialId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/** Product materials repository. */
@ApplicationScoped
public class ProductMaterialRepository
    implements PanacheRepositoryBase<ProductMaterialEntity, ProductMaterialId> {

  public List<ProductMaterialEntity> listByProductId(Long productId) {
    return find("product.id", productId).list();
  }

  public long deleteByProductId(Long productId) {
    return delete("product.id", productId);
  }

  public long deleteByRawMaterialId(Long rawMaterialId) {
    return delete("rawMaterial.id", rawMaterialId);
  }
}
