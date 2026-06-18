package com.dndbattleapp;

import com.dndbatleapp.domain.attribute.Attribute;
import com.dndbatleapp.domain.creature.CreatureStats;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreatureStatsTest {

  @Test
  void modifierForStrength16IsPlus3() {
    var stats = new CreatureStats(Map.of(Attribute.STRENGTH, 16));

    int modifier = stats.modifier(Attribute.STRENGTH);

    assertEquals(3, modifier);
  }
}