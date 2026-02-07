package com.alphasteel.alphaapi.exception;

import com.alphasteel.alphaapi.common.ApiError;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.Instant;

/** Maps exceptions to HTTP responses. */
public final class GlobalExceptionMappers {

  private GlobalExceptionMappers() {}

  @Provider
  public static class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException ex) {
      return build(Response.Status.NOT_FOUND, ex.getMessage());
    }

    private Response build(Response.Status status, String message) {
      ApiError error = new ApiError(
          Instant.now(),
          status.getStatusCode(),
          status.getReasonPhrase(),
          message,
          uriInfo.getPath()
      );
      return Response.status(status).entity(error).build();
    }
  }

  @Provider
  public static class ConflictMapper implements ExceptionMapper<ConflictException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConflictException ex) {
      return build(Response.Status.CONFLICT, ex.getMessage());
    }

    private Response build(Response.Status status, String message) {
      ApiError error = new ApiError(
          Instant.now(),
          status.getStatusCode(),
          status.getReasonPhrase(),
          message,
          uriInfo.getPath()
      );
      return Response.status(status).entity(error).build();
    }
  }

  @Provider
  public static class UnauthorizedMapper implements ExceptionMapper<UnauthorizedException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(UnauthorizedException ex) {
      return build(Response.Status.UNAUTHORIZED, ex.getMessage());
    }

    private Response build(Response.Status status, String message) {
      ApiError error = new ApiError(
          Instant.now(),
          status.getStatusCode(),
          status.getReasonPhrase(),
          message,
          uriInfo.getPath()
      );
      return Response.status(status).entity(error).build();
    }
  }

  @Provider
  public static class GenericMapper implements ExceptionMapper<RuntimeException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(RuntimeException ex) {
      ApiError error = new ApiError(
          Instant.now(),
          500,
          "Internal Server Error",
          "Unexpected error.",
          uriInfo.getPath()
      );
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
  }
}
