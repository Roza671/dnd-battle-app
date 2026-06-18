package com.dndbattleapp.combat;

import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceType;
import com.dndbatleapp.domain.dice.RollResult;
import com.dndbatleapp.domain.shared.Random;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiceRollerTest {

  @Test
  void poolSumsDiceAndAddsModifier() {

    Random fixed = sides -> 4;

    var pool = new DicePool(2, DiceType.D6, 3);

    RollResult result = pool.roll(fixed);

    assertEquals(11, result.total());
    assertEquals(List.of(4, 4), result.rolls());
  }
}
