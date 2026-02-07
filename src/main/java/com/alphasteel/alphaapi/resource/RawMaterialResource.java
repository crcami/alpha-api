package com.alphasteel.alphaapi.resource;

import com.alphasteel.alphaapi.domain.dto.RawMaterialCreateRequest;
import com.alphasteel.alphaapi.domain.dto.RawMaterialResponse;
import com.alphasteel.alphaapi.domain.dto.RawMaterialUpdateRequest;
import com.alphasteel.alphaapi.domain.entity.RawMaterialEntity;
import com.alphasteel.alphaapi.service.RawMaterialService;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;

/** Raw material endpoints. */
@Path("/api/v1/raw-materials")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Raw Materials")
@RolesAllowed("USER")
public class RawMaterialResource {

  @Inject
  RawMaterialService rawMaterialService;

  @GET
  public List<RawMaterialResponse> list() {
    return rawMaterialService.listAll().stream()
        .map(rawMaterialService::toResponse)
        .collect(Collectors.toList());
  }

  @GET
  @Path("/{id}")
  public RawMaterialResponse get(@PathParam("id") Long id) {
    RawMaterialEntity entity = rawMaterialService.getOrThrow(id);
    return rawMaterialService.toResponse(entity);
  }

  @POST
  public Response create(@Valid RawMaterialCreateRequest request) {
    RawMaterialResponse created = rawMaterialService.create(request);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}")
  public RawMaterialResponse update(
      @PathParam("id") Long id,
      @Valid RawMaterialUpdateRequest request
  ) {
    return rawMaterialService.update(id, request);
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    rawMaterialService.delete(id);
    return Response.noContent().build();
  }
}
