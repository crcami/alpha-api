package com.alphasteel.alphaapi.resource;

import com.alphasteel.alphaapi.domain.dto.ProductBomItemResponse;
import com.alphasteel.alphaapi.domain.dto.ProductCreateRequest;
import com.alphasteel.alphaapi.domain.dto.ProductMaterialUpsertRequest;
import com.alphasteel.alphaapi.domain.dto.ProductResponse;
import com.alphasteel.alphaapi.domain.dto.ProductUpdateRequest;
import com.alphasteel.alphaapi.domain.entity.ProductEntity;
import com.alphasteel.alphaapi.service.ProductBomService;
import com.alphasteel.alphaapi.service.ProductService;
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

/** Product endpoints. */
@Path("/api/v1/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Products")
@RolesAllowed("USER")
public class ProductResource {

  @Inject
  ProductService productService;

  @Inject
  ProductBomService productBomService;

  @GET
  public List<ProductResponse> list() {
    return productService.listAll().stream()
        .map(productService::toResponse)
        .collect(Collectors.toList());
  }

  @GET
  @Path("/{id}")
  public ProductResponse get(@PathParam("id") Long id) {
    ProductEntity entity = productService.getOrThrow(id);
    return productService.toResponse(entity);
  }

  @POST
  public Response create(@Valid ProductCreateRequest request) {
    ProductResponse created = productService.create(request);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}")
  public ProductResponse update(@PathParam("id") Long id, @Valid ProductUpdateRequest request) {
    return productService.update(id, request);
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    productService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/{id}/materials")
  public List<ProductBomItemResponse> listMaterials(@PathParam("id") Long id) {
    return productBomService.listByProduct(id);
  }

  @PUT
  @Path("/{id}/materials")
  public List<ProductBomItemResponse> replaceMaterials(
      @PathParam("id") Long id,
      @Valid List<ProductMaterialUpsertRequest> items
  ) {
    return productBomService.replaceMaterials(id, items);
  }
}
