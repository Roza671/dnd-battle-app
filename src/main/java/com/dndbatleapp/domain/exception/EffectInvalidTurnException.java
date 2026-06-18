package com.dndbatleapp.domain.exception;

public class EffectInvalidTurnException extends RuntimeException {
  public EffectInvalidTurnException() {
    super("Effect should be applied more than 0 turns.");
  }
}
