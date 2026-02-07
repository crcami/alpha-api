package com.alphasteel.alphaapi.repository;

import com.alphasteel.alphaapi.domain.entity.RawMaterialEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/** Raw material repository. */
@ApplicationScoped
public class RawMaterialRepository implements PanacheRepositoryBase<RawMaterialEntity, Long> {

  public Optional<RawMaterialEntity> findByCode(String code) {
    return find("code", code).firstResultOptional();
  }
}
