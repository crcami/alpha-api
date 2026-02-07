package com.alphasteel.alphaapi.security.repository;

import com.alphasteel.alphaapi.security.entity.AppUserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/** User repository. */
@ApplicationScoped
public class AppUserRepository implements PanacheRepositoryBase<AppUserEntity, Long> {

  public Optional<AppUserEntity> findByEmail(String email) {
    return find("email", email.toLowerCase()).firstResultOptional();
  }
}
