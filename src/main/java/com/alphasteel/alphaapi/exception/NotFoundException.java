package com.alphasteel.alphaapi.exception;

/** Thrown when a resource is not found. */
public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
