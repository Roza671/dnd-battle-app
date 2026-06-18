package com.dndnbatleapp.adapter.in.content;

import com.dndnbatleapp.domain.attribute.Attribute;
import com.dndnbatleapp.domain.creature.Creature;
import com.dndnbatleapp.domain.creature.CreatureStats;

import java.util.Map;

public class Creatures {

  public static Creature orc(String name) {
    var creatureStats = createStats(16, 10, 8);

    return new Creature(name, 26, 16, creatureStats);
  }

  public static Creature mage(String name) {
    var creatureStats = createStats(8, 8, 16);

    return new Creature(name, 11, 10, creatureStats);
  }

  public static Creature goblin() {
    var creatureStats = createStats(8, 14, 6);

    return new Creature("Гоблыч", 16, 12, creatureStats);
  }

  public static Creature skeleton() {
    var creatureStats = createStats(12, 12, 6);

    return new Creature("Костян", 14, 12, creatureStats);
  }

  public static Creature dragon() {
    var creatureStats = createStats(25, 14, 20);

    return new Creature("Дракоша", 1200, 12, creatureStats);
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
