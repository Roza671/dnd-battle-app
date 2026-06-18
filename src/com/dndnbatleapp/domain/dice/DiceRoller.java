package com.dndnbatleapp.domain.dice;

@FunctionalInterface
public interface DiceRoller {
  int roll(int sides);
}
