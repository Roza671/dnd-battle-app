package com.dndbatleapp.domain.creature;

import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.exception.DeadCreatureException;

import static com.dndbatleapp.domain.attribute.Attribute.*;

public class Creature {
  private final String name;
  private final int maxHp;
  private final int maxMana;
  private final int armorClass;
  private final CreatureStats stats;
  private final DicePool damagePool;
  private int currentHp;
  private int currentMana;

  public Creature(String name, int maxHp, int maxMana, int armorClass, CreatureStats stats, DicePool damagePool) {
    this.name = name;
    this.maxHp = maxHp;
    this.currentHp = maxHp;
    this.maxMana = maxMana;
    this.currentMana = maxMana;
    this.armorClass = armorClass;
    this.stats = stats;
    this.damagePool = damagePool;
  }

  public void heal(int amount) {
    if (! isAlive()) {
      throw new DeadCreatureException("Cannot heal dead creature.");
    }

    if ((currentHp + amount) > maxHp) {
      currentHp = maxHp;
      return;
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

  public void takeMana(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Take mana cannot be less than 0.");
    }

    currentMana -= amount;

    if (currentMana < 0) {
      currentMana = 0;
    }
  }

  public boolean isAlive() {
    return currentHp > 0;
  }

  public Integer attackBonus() {
    return stats.modifier(STRENGTH);
  }

  public Integer spellBonus() {
    return stats.modifier(INTELLIGENCE);
  }

  public int initiativeBonus() {
    return stats.modifier(DEXTERITY);
  }

  public int maxHp() {
    return maxHp;
  }

  public int currentHp() {
    return currentHp;
  }

  public int maxMana() {
    return maxMana;
  }

  public int currentMana() {
    return currentMana;
  }

  public int armorClass() {
    return armorClass;
  }

  public String name() {
    return name;
  }

  public DicePool damagePool() {
    return damagePool;
  }
}
