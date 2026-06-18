package com.dndnbatleapp.adapter.out.dice;

import com.dndnbatleapp.domain.dice.DiceRoller;

import java.util.random.RandomGenerator;

public class RandomDiceRoller implements DiceRoller {
  private final RandomGenerator random;

  public RandomDiceRoller() {
    this(RandomGenerator.getDefault());
  }

  public RandomDiceRoller(RandomGenerator random) {
    this.random = random;
  }

  @Override
  public int roll(int sides) {
    if (sides < 1) {
      throw new IllegalArgumentException("Sides should be more than 1, got: " + sides);
    }

    return random.nextInt(sides) + 1;
  }
}
