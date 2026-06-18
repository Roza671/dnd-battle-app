package com.dndbatleapp.domain.dice;

import com.dndbatleapp.domain.exception.DicePoolCountException;

public record DicePool(int count, DiceType type, int modifier) {
  public DicePool {
    if (count <= 0) {
      throw new DicePoolCountException();
    }
  }
}
