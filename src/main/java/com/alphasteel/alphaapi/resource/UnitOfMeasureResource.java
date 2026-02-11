package com.alphasteel.alphaapi.resource;

import com.alphasteel.alphaapi.domain.dto.UnitOfMeasureCreateRequest;
import com.alphasteel.alphaapi.domain.dto.UnitOfMeasureResponse;
import com.alphasteel.alphaapi.domain.dto.UnitOfMeasureUpdateRequest;
import com.alphasteel.alphaapi.service.UnitOfMeasureService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/** Unit of measure resource. */
@Path("/api/v1/units-of-measure")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Units of Measure")
@RolesAllowed("USER")
public class UnitOfMeasureResource {

  @Inject
  UnitOfMeasureService service;

  @GET
  public List<UnitOfMeasureResponse> list() {
    return service.listAll();
  }

  @POST
  public Response create(@Valid UnitOfMeasureCreateRequest request) {
    UnitOfMeasureResponse response = service.create(request);
    return Response.status(Response.Status.CREATED).entity(response).build();
  }

  @PUT
  @Path("/{id}")
  public UnitOfMeasureResponse update(
      @PathParam("id") Long id,
      @Valid UnitOfMeasureUpdateRequest request) {
    return service.update(id, request);
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    service.delete(id);
    return Response.noContent().build();
  }
}
