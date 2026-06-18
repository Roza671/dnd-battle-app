package com.dndnbatleapp.domain.dice;

import com.dndnbatleapp.domain.exception.DicePoolCountException;

public record DicePool(int count, DiceType type, int modifier) {
  public DicePool {
    if (count <= 0) {
      throw new DicePoolCountException();
    }
  }
}
