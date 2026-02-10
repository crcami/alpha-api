package com.alphasteel.alphaapi.service;

import com.alphasteel.alphaapi.domain.dto.ProductionSuggestionItemResponse;
import com.alphasteel.alphaapi.domain.dto.ProductionSuggestionResponse;
import com.alphasteel.alphaapi.domain.entity.ProductEntity;
import com.alphasteel.alphaapi.domain.entity.ProductMaterialEntity;
import com.alphasteel.alphaapi.domain.entity.RawMaterialEntity;
import com.alphasteel.alphaapi.repository.ProductMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Production planner service. */
@ApplicationScoped
public class ProductionPlannerService {

  @Inject
  ProductService productService;

  @Inject
  RawMaterialService rawMaterialService;

  @Inject
  ProductMaterialRepository productMaterialRepository;

  public ProductionSuggestionResponse suggest() {
    List<ProductEntity> products = new ArrayList<>(productService.listAll());
    products.sort(Comparator.comparing((ProductEntity p) -> p.value).reversed());

    Map<Long, BigDecimal> stockByMaterial = new HashMap<>();
    for (RawMaterialEntity rm : rawMaterialService.listAll()) {
      stockByMaterial.put(rm.id, rm.stockQty);
    }

    List<ProductionSuggestionItemResponse> items = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (ProductEntity product : products) {
      List<ProductMaterialEntity> bom = productMaterialRepository.listByProductId(product.id);
      if (bom.isEmpty()) {
        continue;
      }

      long maxQty = computeMaxQty(bom, stockByMaterial);
      if (maxQty <= 0) {
        continue;
      }

      consumeStock(bom, stockByMaterial, maxQty);

      BigDecimal itemTotal = product.value.multiply(BigDecimal.valueOf(maxQty));
      total = total.add(itemTotal);

      items.add(new ProductionSuggestionItemResponse(
          product.id,
          product.code,
          product.name,
          product.value,
          maxQty,
          itemTotal
      ));
    }

    return new ProductionSuggestionResponse(items, total);
  }

  private long computeMaxQty(List<ProductMaterialEntity> bom, Map<Long, BigDecimal> stock) {
    long max = Long.MAX_VALUE;

    for (ProductMaterialEntity item : bom) {
      BigDecimal available = stock.getOrDefault(item.rawMaterial.id, BigDecimal.ZERO);
      BigDecimal required = item.quantityRequired;

      if (required.signum() <= 0) {
        continue;
      }

      BigDecimal possible = available.divide(required, 0, RoundingMode.FLOOR);
      long p = possible.longValueExact();

      if (p < max) {
        max = p;
      }
    }

    return max == Long.MAX_VALUE ? 0 : max;
  }

  private void consumeStock(
      List<ProductMaterialEntity> bom,
      Map<Long, BigDecimal> stock,
      long qty
  ) {
    for (ProductMaterialEntity item : bom) {
      BigDecimal available = stock.getOrDefault(item.rawMaterial.id, BigDecimal.ZERO);
      BigDecimal required = item.quantityRequired.multiply(BigDecimal.valueOf(qty));
      stock.put(item.rawMaterial.id, available.subtract(required));
    }
  }
}
