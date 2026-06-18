package com.dndbatleapp.domain.item;

import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.shared.Identifiable;

public sealed interface Item extends Identifiable<String> {
  record Weapon(String id, String name, DicePool damage, int attackBonus) implements Item {
  }

  record Armor(String id, String name, int armorBonus) implements Item {
  }

  record Potion(String id, String name, PotionEffect effect, int power) implements Item {
  }
}
