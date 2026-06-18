package com.dndbatleapp.adapter.out.random;

import com.dndbatleapp.domain.shared.Random;

import java.util.random.RandomGenerator;

public class DefaultRandom implements Random {
  private final RandomGenerator random;

  public DefaultRandom() {
    this(RandomGenerator.getDefault());
  }

  public DefaultRandom(RandomGenerator random) {
    this.random = random;
  }

  @Override
  public int next(int max) {
    return random.nextInt(max) + 1;
  }
}
