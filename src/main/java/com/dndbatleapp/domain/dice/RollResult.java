package com.dndbatleapp.domain.dice;

import java.util.List;

public record RollResult(int total, List<Integer> rolls, int modifier) {
  public RollResult {
    rolls = List.copyOf(rolls);
  }
}
