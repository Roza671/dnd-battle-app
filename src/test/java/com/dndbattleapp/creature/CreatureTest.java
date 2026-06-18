package com.dndbattleapp.creature;

import com.dndbatleapp.domain.creature.bestiary.Creatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreatureTest {

  @Test
  void takeDamageDoesNotGoBelowZero() {
    var goblin = Creatures.goblin();
    goblin.takeDamage(9999);
    assertEquals(0, goblin.currentHp());
    assertFalse(goblin.isAlive());
  }

  @Test
  void healDoesNotExceedMaxHp() {
    var hero = Creatures.warrior("ГАТЦС");
    hero.takeDamage(5);
    hero.heal(9999);
    assertEquals(hero.maxHp(), hero.currentHp());
  }

  @Test
  void takeNegativeDamageIsRejected() {
    var hero = Creatures.warrior("ГАТЦС");
    assertThrows(IllegalArgumentException.class, () -> hero.takeDamage(-10));
  }
}
