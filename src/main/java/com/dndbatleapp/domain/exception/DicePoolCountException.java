package com.dndbatleapp.domain.exception;

public class DicePoolCountException extends RuntimeException {
  public DicePoolCountException() {
    super("Count should be more than 0.");
  }
}
