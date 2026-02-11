package com.alphasteel.alphaapi.repository;

import com.alphasteel.alphaapi.domain.entity.UnitOfMeasureEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UnitOfMeasureRepository implements PanacheRepository<UnitOfMeasureEntity> {
  
  public Optional<UnitOfMeasureEntity> findByCode(String code) {
    return find("code", code).firstResultOptional();
  }
}
