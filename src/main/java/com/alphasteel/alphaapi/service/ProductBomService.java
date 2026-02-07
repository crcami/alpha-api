package com.alphasteel.alphaapi.service;

import com.alphasteel.alphaapi.domain.dto.ProductBomItemResponse;
import com.alphasteel.alphaapi.domain.dto.ProductMaterialUpsertRequest;
import com.alphasteel.alphaapi.domain.entity.ProductEntity;
import com.alphasteel.alphaapi.domain.entity.ProductMaterialEntity;
import com.alphasteel.alphaapi.domain.entity.ProductMaterialId;
import com.alphasteel.alphaapi.domain.entity.RawMaterialEntity;
import com.alphasteel.alphaapi.repository.ProductMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** BOM service. */
@ApplicationScoped
public class ProductBomService {

  @Inject
  ProductService productService;

  @Inject
  RawMaterialService rawMaterialService;

  @Inject
  ProductMaterialRepository productMaterialRepository;

  public List<ProductBomItemResponse> listByProduct(Long productId) {
    return productMaterialRepository.listByProductId(productId).stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public List<ProductBomItemResponse> replaceMaterials(
      Long productId,
      List<ProductMaterialUpsertRequest> items
  ) {
    ProductEntity product = productService.getOrThrow(productId);

    List<ProductMaterialEntity> existing = productMaterialRepository.listByProductId(productId);
    Map<Long, ProductMaterialEntity> byMaterialId = new HashMap<>();

    for (ProductMaterialEntity e : existing) {
      byMaterialId.put(e.rawMaterial.id, e);
    }

    Map<Long, Boolean> keep = new HashMap<>();

    for (ProductMaterialUpsertRequest req : items) {
      RawMaterialEntity material = rawMaterialService.getOrThrow(req.rawMaterialId());
      ProductMaterialEntity entity = byMaterialId.get(material.id);

      if (entity == null) {
        entity = new ProductMaterialEntity();
        entity.id = new ProductMaterialId(product.id, material.id);
        entity.product = product;
        entity.rawMaterial = material;
        productMaterialRepository.persist(entity);
      }

      entity.quantityRequired = req.quantityRequired();
      keep.put(material.id, true);
    }

    for (ProductMaterialEntity e : existing) {
      if (!keep.containsKey(e.rawMaterial.id)) {
        productMaterialRepository.delete(e);
      }
    }

    return productMaterialRepository.listByProductId(productId).stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  private ProductBomItemResponse toResponse(ProductMaterialEntity entity) {
    return new ProductBomItemResponse(
        entity.rawMaterial.id,
        entity.rawMaterial.code,
        entity.rawMaterial.name,
        entity.quantityRequired
    );
  }
}
