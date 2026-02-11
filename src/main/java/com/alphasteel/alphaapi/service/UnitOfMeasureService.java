package com.alphasteel.alphaapi.service;

import com.alphasteel.alphaapi.domain.dto.UnitOfMeasureCreateRequest;
import com.alphasteel.alphaapi.domain.dto.UnitOfMeasureResponse;
import com.alphasteel.alphaapi.domain.dto.UnitOfMeasureUpdateRequest;
import com.alphasteel.alphaapi.domain.entity.UnitOfMeasureEntity;
import com.alphasteel.alphaapi.exception.ConflictException;
import com.alphasteel.alphaapi.exception.NotFoundException;
import com.alphasteel.alphaapi.repository.UnitOfMeasureRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/** Unit of measure service. */
@ApplicationScoped
public class UnitOfMeasureService {

  @Inject
  UnitOfMeasureRepository repository;

  public List<UnitOfMeasureResponse> listAll() {
    return repository.listAll().stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  public UnitOfMeasureEntity getOrThrow(Long id) {
    return repository.findByIdOptional(id)
        .orElseThrow(() -> new NotFoundException("Unidade de medida não encontrada."));
  }

  @Transactional
  public UnitOfMeasureResponse create(UnitOfMeasureCreateRequest request) {
    String code = request.code().trim().toUpperCase();
    
    if (repository.findByCode(code).isPresent()) {
      throw new ConflictException("Já existe uma unidade de medida com o código: " + code);
    }

    UnitOfMeasureEntity entity = new UnitOfMeasureEntity();
    entity.code = code;
    entity.name = request.name().trim();

    repository.persist(entity);
    return toResponse(entity);
  }

  @Transactional
  public UnitOfMeasureResponse update(Long id, UnitOfMeasureUpdateRequest request) {
    UnitOfMeasureEntity entity = getOrThrow(id);

    String code = request.code().trim().toUpperCase();
    
    // Verificar se o código já existe em outra unidade
    repository.findByCode(code).ifPresent(existing -> {
      if (!existing.id.equals(entity.id)) {
        throw new ConflictException("Já existe uma unidade de medida com o código: " + code);
      }
    });

    entity.code = code;
    entity.name = request.name().trim();

    return toResponse(entity);
  }

  @Transactional
  public void delete(Long id) {
    UnitOfMeasureEntity entity = getOrThrow(id);
    
    repository.delete(entity);
  }

  private UnitOfMeasureResponse toResponse(UnitOfMeasureEntity entity) {
    return new UnitOfMeasureResponse(entity.id, entity.code, entity.name);
  }
}
