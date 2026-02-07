package com.alphasteel.alphaapi.repository;

import com.alphasteel.alphaapi.domain.entity.ProductEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/** Product repository. */
@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductEntity, Long> {

  public Optional<ProductEntity> findByCode(String code) {
    return find("code", code).firstResultOptional();
  }
}
