package com.alphasteel.alphaapi.security.resource;

import com.alphasteel.alphaapi.security.dto.AuthLoginRequest;
import com.alphasteel.alphaapi.security.dto.AuthRegisterRequest;
import com.alphasteel.alphaapi.security.dto.AuthRegisterResponse;
import com.alphasteel.alphaapi.security.dto.AuthTokenResponse;
import com.alphasteel.alphaapi.security.dto.ForgotPasswordRequest;
import com.alphasteel.alphaapi.security.dto.ResetPasswordRequest;
import com.alphasteel.alphaapi.security.entity.AppUserEntity;
import com.alphasteel.alphaapi.security.service.AuthService;
import com.alphasteel.alphaapi.security.service.PasswordResetService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/** Auth endpoints. */
@Path("/api/v1/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Auth")
public class AuthResource {

  @Inject
  AuthService authService;

  @Inject
  PasswordResetService passwordResetService;

  @POST
  @Path("/register")
  @PermitAll
  public Response register(@Valid AuthRegisterRequest request) {
    AppUserEntity user = authService.register(request.name(), request.email(), request.password());

    AuthRegisterResponse payload = new AuthRegisterResponse(
        user.id,
        user.name,
        user.email
    );

    return Response.status(Response.Status.CREATED).entity(payload).build();
  }

  @POST
  @Path("/login")
  @PermitAll
  public AuthTokenResponse login(@Valid AuthLoginRequest request) {
    String token = authService.login(request.email(), request.password());
    return new AuthTokenResponse("Bearer", token, 3600);
  }

  @POST
  @Path("/forgot-password")
  @PermitAll
  public Response forgotPassword(@Valid ForgotPasswordRequest request) {
    passwordResetService.requestReset(request.email());
    return Response.noContent().build();
  }

  @POST
  @Path("/reset-password")
  @PermitAll
  public Response resetPassword(@Valid ResetPasswordRequest request) {
    passwordResetService.resetPassword(request.token(), request.newPassword());
    return Response.noContent().build();
  }
}
