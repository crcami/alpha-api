package com.alphasteel.alphaapi.common;

import com.alphasteel.alphaapi.domain.entity.UnitOfMeasureEntity;
import com.alphasteel.alphaapi.repository.UnitOfMeasureRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataSeeder {

  @Inject
  UnitOfMeasureRepository uomRepository;

  @Transactional
  void onStart(@Observes StartupEvent ev) {
    seedUnitsOfMeasure();
  }

  private void seedUnitsOfMeasure() {
    if (uomRepository.count() == 0) {
      createUom("UN", "Unidade");
      createUom("KG", "Kilograma");
      createUom("G", "Grama");
      createUom("L", "Litro");
      createUom("ML", "Mililitro");
    }
  }

  private void createUom(String code, String name) {
    UnitOfMeasureEntity uom = new UnitOfMeasureEntity();
    uom.code = code;
    uom.name = name;
    uomRepository.persist(uom);
  }
}
