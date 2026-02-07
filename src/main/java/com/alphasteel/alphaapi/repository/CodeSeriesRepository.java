package com.alphasteel.alphaapi.repository;

import com.alphasteel.alphaapi.domain.CodeType;
import com.alphasteel.alphaapi.domain.entity.CodeSeriesEntity;
import com.alphasteel.alphaapi.domain.entity.CodeSeriesId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.Optional;

/** Code series repository. */
@ApplicationScoped
public class CodeSeriesRepository implements PanacheRepositoryBase<CodeSeriesEntity, CodeSeriesId> {

  @Transactional
  public long nextFor(CodeType type, String prefix) {
    CodeSeriesId id = new CodeSeriesId(type, prefix);

    Optional<CodeSeriesEntity> existing =
        find("id", id).withLock(LockModeType.PESSIMISTIC_WRITE).firstResultOptional();

    CodeSeriesEntity entity =
        existing.orElseGet(
            () -> {
              CodeSeriesEntity created = new CodeSeriesEntity();
              created.id = id;
              created.nextNumber = 1L;
              persist(created);
              return created;
            });

    long current = entity.nextNumber;
    entity.nextNumber = current + 1L;
    return current;
  }
}
