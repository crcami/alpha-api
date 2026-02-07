package com.alphasteel.alphaapi.exception;

/** Thrown when authentication fails. */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String message) {
    super(message);
  }
}
