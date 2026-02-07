package com.alphasteel.alphaapi.security.resource;

import com.alphasteel.alphaapi.exception.NotFoundException;
import com.alphasteel.alphaapi.security.dto.ChangePasswordRequest;
import com.alphasteel.alphaapi.security.entity.AppUserEntity;
import com.alphasteel.alphaapi.security.repository.AppUserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/** Profile endpoints. */
@Path("/api/v1/me")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Me")
public class MeResource {

  @Inject
  SecurityIdentity securityIdentity;

  @Inject
  AppUserRepository appUserRepository;

  @GET
  @RolesAllowed("USER")
  public Response me() {
    String email = securityIdentity.getPrincipal().getName();
    return Response.ok(new MeResponse(email)).build();
  }

  @PUT
  @Path("/password")
  @RolesAllowed("USER")
  @Transactional
  public Response changePassword(@Valid ChangePasswordRequest request) {
    String email = securityIdentity.getPrincipal().getName();

    AppUserEntity user = appUserRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("User not found."));

    if (!BcryptUtil.matches(request.currentPassword(), user.passwordHash)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    user.passwordHash = BcryptUtil.bcryptHash(request.newPassword());
    return Response.noContent().build();
  }

  /** Profile response. */
  public record MeResponse(String email) {}
}
