package com.dndbatleapp.domain.combat.effect;

import com.dndbatleapp.domain.exception.EffectInvalidTurnException;

public record Effect(EffectType type, int remainingTurns, int power) {

  public Effect {
    if (remainingTurns < 0) {
      throw new EffectInvalidTurnException();
    }
  }

  public Effect tickedDown() {
    return new Effect(type, remainingTurns - 1, power);
  }

  public boolean expired() {
    return remainingTurns <= 0;
  }
}
