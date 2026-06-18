package com.dndbatleapp.domain.creature;

import com.dndbatleapp.domain.exception.DeadCreatureException;

import static com.dndbatleapp.domain.attribute.Attribute.STRENGTH;

public class Creature {
  private final String name;
  private int maxHp;
  private int currentHp;
  private int armorClass;
  private final CreatureStats stats;

  public Creature(String name, int maxHp, int armorClass, CreatureStats stats) {
    this.name = name;
    this.maxHp = maxHp;
    this.currentHp = maxHp;
    this.armorClass = armorClass;
    this.stats = stats;
  }

  public void heal(int amount) {
    if (! isAlive()) {
      throw new DeadCreatureException("Cannot heal dead creature.");
    }

    if ((currentHp + amount) > maxHp) {
      currentHp = maxHp;
    }

    currentHp += amount;
  }

  public void takeDamage(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Damage cannot be less than 0.");
    }

    currentHp -= amount;

    if (currentHp < 0) {
      currentHp = 0;
    }
  }

  public boolean isAlive() {
    return currentHp > 0;
  }

  public Integer attackBonus() {
    int strength = stats.get().getOrDefault(STRENGTH, 10);

    return Math.floorDiv(strength - 10, 2);
  }

}
