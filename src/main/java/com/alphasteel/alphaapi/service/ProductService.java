package com.alphasteel.alphaapi.service;

import com.alphasteel.alphaapi.common.CodeGenerator;
import com.alphasteel.alphaapi.domain.CodeType;
import com.alphasteel.alphaapi.domain.dto.ProductCreateRequest;
import com.alphasteel.alphaapi.domain.dto.ProductResponse;
import com.alphasteel.alphaapi.domain.dto.ProductUpdateRequest;
import com.alphasteel.alphaapi.domain.entity.ProductEntity;
import com.alphasteel.alphaapi.exception.ConflictException;
import com.alphasteel.alphaapi.exception.NotFoundException;
import com.alphasteel.alphaapi.repository.ProductMaterialRepository;
import com.alphasteel.alphaapi.repository.ProductRepository;
import com.alphasteel.alphaapi.repository.UnitOfMeasureRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/** Product service. */
@ApplicationScoped
public class ProductService {

  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMaterialRepository productMaterialRepository;

  @Inject
  CodeGenerator codeGenerator;

  @Inject
  UnitOfMeasureRepository unitOfMeasureRepository;

  public List<ProductEntity> listAll() {
    return productRepository.listAll();
  }

  public ProductEntity getOrThrow(Long id) {
    return productRepository.findByIdOptional(id)
        .orElseThrow(() -> new NotFoundException("Product not found."));
  }

  @Transactional
  public ProductResponse create(ProductCreateRequest request) {
    String code = resolveCode(request.code(), request.name(), CodeType.PRODUCT);

    if (productRepository.findByCode(code).isPresent()) {
      throw new ConflictException("Product code already exists.");
    }

    ProductEntity entity = new ProductEntity();
    entity.code = code;
    entity.name = request.name().trim();
    entity.value = request.value();
    
    // Buscar UnitOfMeasure do banco ou usar padrão
    String uomCode = request.unitOfMeasure() != null ? request.unitOfMeasure() : "UN";
    entity.unitOfMeasure = unitOfMeasureRepository.findByCode(uomCode)
        .orElseThrow(() -> new NotFoundException("Unit of measure not found: " + uomCode));

    productRepository.persist(entity);
    return toResponse(entity);
  }

  @Transactional
  public ProductResponse update(Long id, ProductUpdateRequest request) {
    ProductEntity entity = getOrThrow(id);

    String code = resolveCode(request.code(), request.name(), CodeType.PRODUCT);
    productRepository.findByCode(code).ifPresent(existing -> {
      if (!existing.id.equals(entity.id)) {
        throw new ConflictException("Product code already exists.");
      }
    });

    entity.code = code;
    entity.name = request.name().trim();
    entity.value = request.value();
    
    if (request.unitOfMeasure() != null) {
      entity.unitOfMeasure = unitOfMeasureRepository.findByCode(request.unitOfMeasure())
          .orElseThrow(() -> new NotFoundException("Unit of measure not found: " + request.unitOfMeasure()));
    }

    return toResponse(entity);
  }

  @Transactional
  public void delete(Long id) {
    ProductEntity entity = getOrThrow(id);
    productMaterialRepository.deleteByProductId(id);
    productRepository.delete(entity);
  }

  public ProductResponse toResponse(ProductEntity entity) {
    String uomCode = entity.unitOfMeasure != null ? entity.unitOfMeasure.code : null;
    return new ProductResponse(entity.id, entity.code, entity.name, entity.value, uomCode);
  }

  private String resolveCode(String code, String name, CodeType type) {
    if (code != null && !code.isBlank()) {
      return code.trim().toUpperCase();
    }
    return codeGenerator.generateCode(type, name);
  }
}
