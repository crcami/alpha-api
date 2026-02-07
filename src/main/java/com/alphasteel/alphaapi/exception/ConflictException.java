package com.alphasteel.alphaapi.exception;

/** Thrown when a conflict occurs. */
public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }
}
