package com.dndbatleapp.domain.dice;

import com.dndbatleapp.domain.exception.DicePoolCountException;

import java.util.ArrayList;
import java.util.List;

public record DicePool(int count, DiceType type, int modifier) {
  public DicePool {
    if (count <= 0) {
      throw new DicePoolCountException();
    }
  }

  public RollResult roll(DiceRoller roller) {
    List<Integer> rolls = new ArrayList<>(count);
    int diceSum = 0;
    for (int i = 0; i < count; i++) {
      int r = roller.roll(type.sides());
      rolls.add(r);
      diceSum += r;
    }
    return new RollResult(diceSum + modifier, List.copyOf(rolls), modifier);
  }
}
