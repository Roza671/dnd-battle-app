package com.dndbatleapp.domain.dice;

@FunctionalInterface
public interface DiceRoller {
  int roll(int sides);
}
