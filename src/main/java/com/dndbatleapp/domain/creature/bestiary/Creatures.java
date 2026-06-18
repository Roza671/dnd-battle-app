package com.dndbatleapp.domain.creature.bestiary;

import com.dndbatleapp.domain.attribute.Attribute;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.creature.CreatureStats;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceType;

import java.util.Map;

public class Creatures {

  public static Creature warrior(String name) {
    var creatureStats = createStats(16, 10, 8);
    var damagePool = new DicePool(2, DiceType.D12, 0);

    return new Creature(name, 26, 0, 16, creatureStats, damagePool);
  }

  public static Creature mage(String name) {
    var creatureStats = createStats(8, 8, 18);
    var damagePool = new DicePool(1, DiceType.D10, 0);

    return new Creature(name, 11, 15, 10, creatureStats, damagePool);
  }

  public static Creature cleric(String name) {
    var creatureStats = createStats(8, 8, 16);
    var damagePool = new DicePool(1, DiceType.D6, 0);

    return new Creature(name, 13, 15, 10, creatureStats, damagePool);
  }

  public static Creature goblin() {
    var creatureStats = createStats(8, 14, 6);
    var damagePool = new DicePool(1, DiceType.D4, 0);

    return new Creature("Гоблыч", 16, 0, 12, creatureStats, damagePool);
  }

  public static Creature dragon() {
    var creatureStats = createStats(25, 14, 20);
    var damagePool = new DicePool(4, DiceType.D20, 0);

    return new Creature("Дракоша", 120, 1000, 12, creatureStats, damagePool);
  }

  private static CreatureStats createStats(int strength, int dexterity, int intelligence) {
    var stats = Map.of(
        Attribute.STRENGTH, strength,
        Attribute.DEXTERITY, dexterity,
        Attribute.INTELLIGENCE, intelligence
    );

    return new CreatureStats(stats);
  }
}
