package com.dndbattleapp.creature;

import com.dndbatleapp.domain.attribute.Attribute;
import com.dndbatleapp.domain.creature.CreatureStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreatureStatsTest {

  @Test
  void modifierForStrength16IsPlus3() {
    var stats = new CreatureStats(Map.of(Attribute.STRENGTH, 16));

    int modifier = stats.modifier(Attribute.STRENGTH);

    assertEquals(3, modifier);
  }

  @ParameterizedTest
  @CsvSource({
      "10, 0",
      "16, 3",
      "8, -1",
      "1, -5",
      "20, 5"
  })
  void attributeModifierIsCorrect(int value, int expectedModifier) {
    var stats = new CreatureStats(Map.of(Attribute.STRENGTH, value));
    assertEquals(expectedModifier, stats.modifier(Attribute.STRENGTH));
  }
}