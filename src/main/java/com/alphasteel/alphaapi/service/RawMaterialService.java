package com.alphasteel.alphaapi.service;

import com.alphasteel.alphaapi.common.CodeGenerator;
import com.alphasteel.alphaapi.domain.CodeType;
import com.alphasteel.alphaapi.domain.dto.RawMaterialCreateRequest;
import com.alphasteel.alphaapi.domain.dto.RawMaterialResponse;
import com.alphasteel.alphaapi.domain.dto.RawMaterialUpdateRequest;
import com.alphasteel.alphaapi.domain.entity.RawMaterialEntity;
import com.alphasteel.alphaapi.exception.ConflictException;
import com.alphasteel.alphaapi.exception.NotFoundException;
import com.alphasteel.alphaapi.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/** Raw material service. */
@ApplicationScoped
public class RawMaterialService {

  @Inject
  RawMaterialRepository rawMaterialRepository;

  @Inject
  CodeGenerator codeGenerator;

  public List<RawMaterialEntity> listAll() {
    return rawMaterialRepository.listAll();
  }

  public RawMaterialEntity getOrThrow(Long id) {
    return rawMaterialRepository.findByIdOptional(id)
        .orElseThrow(() -> new NotFoundException("Raw material not found."));
  }

  @Transactional
  public RawMaterialResponse create(RawMaterialCreateRequest request) {
    String code = resolveCode(request.code(), request.name(), CodeType.RAW_MATERIAL);

    if (rawMaterialRepository.findByCode(code).isPresent()) {
      throw new ConflictException("Raw material code already exists.");
    }

    RawMaterialEntity entity = new RawMaterialEntity();
    entity.code = code;
    entity.name = request.name().trim();
    entity.stockQuantity = request.stockQuantity();

    rawMaterialRepository.persist(entity);
    return toResponse(entity);
  }

  @Transactional
  public RawMaterialResponse update(Long id, RawMaterialUpdateRequest request) {
    RawMaterialEntity entity = getOrThrow(id);

    String code = resolveCode(request.code(), request.name(), CodeType.RAW_MATERIAL);
    rawMaterialRepository.findByCode(code).ifPresent(existing -> {
      if (!existing.id.equals(entity.id)) {
        throw new ConflictException("Raw material code already exists.");
      }
    });

    entity.code = code;
    entity.name = request.name().trim();
    entity.stockQuantity = request.stockQuantity();

    return toResponse(entity);
  }

  @Transactional
  public void delete(Long id) {
    RawMaterialEntity entity = getOrThrow(id);
    rawMaterialRepository.delete(entity);
  }

  public RawMaterialResponse toResponse(RawMaterialEntity entity) {
    return new RawMaterialResponse(entity.id, entity.code, entity.name, entity.stockQuantity);
  }

  private String resolveCode(String code, String name, CodeType type) {
    if (code != null && !code.isBlank()) {
      return code.trim().toUpperCase();
    }
    return codeGenerator.generateCode(type, name);
  }
}
