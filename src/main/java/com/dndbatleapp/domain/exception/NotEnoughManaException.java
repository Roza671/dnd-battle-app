package com.dndbatleapp.domain.exception;

public class NotEnoughManaException extends RuntimeException {
  public NotEnoughManaException(String message) {
    super(message);
  }
}
