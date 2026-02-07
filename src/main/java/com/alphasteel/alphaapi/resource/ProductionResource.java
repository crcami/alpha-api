package com.alphasteel.alphaapi.resource;

import com.alphasteel.alphaapi.domain.dto.ProductionSuggestionResponse;
import com.alphasteel.alphaapi.service.ProductionPlannerService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/** Production endpoints. */
@Path("/api/v1/production")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Production")
@RolesAllowed("USER")
public class ProductionResource {

  @Inject
  ProductionPlannerService productionPlannerService;

  @GET
  @Path("/suggestions")
  public ProductionSuggestionResponse suggest() {
    return productionPlannerService.suggest();
  }
}
