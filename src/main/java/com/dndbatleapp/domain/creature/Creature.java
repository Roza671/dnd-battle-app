package com.dndbatleapp.domain.creature;

import com.dndbatleapp.domain.combat.effect.Effect;
import com.dndbatleapp.domain.combat.effect.EffectType;
import com.dndbatleapp.domain.combat.strategy.ActionStrategy;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dndbatleapp.domain.attribute.Attribute.*;

public class Creature {
  private final String name;
  private final int maxHp, maxMana, armorClass;
  private final CreatureStats stats;
  private final DicePool damagePool;
  private final ActionStrategy strategy;
  private final List<Effect> effects = new ArrayList<>();
  private int currentHp, currentMana;
  private Item.Weapon equippedWeapon;
  private Item.Armor equippedArmor;

  public Creature(String name, int maxHp, int maxMana, int armorClass, CreatureStats stats, DicePool damagePool, ActionStrategy strategy) {
    this.name = name;
    this.maxHp = maxHp;
    this.currentHp = maxHp;
    this.maxMana = maxMana;
    this.currentMana = maxMana;
    this.armorClass = armorClass;
    this.stats = stats;
    this.damagePool = damagePool;
    this.strategy = strategy;
  }

  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  public List<Effect> effects() {
    return List.copyOf(effects);
  }

  public boolean isStunned() {
    return effects.stream()
        .anyMatch(effect -> effect.type() == EffectType.STUNNED);
  }

  public void tickEffects() {
    List<Effect> updated = effects.stream()
        .map(effect -> {
          if (effect.type() == EffectType.POISONED) {
            takeDamage(effect.power());
          }

          return effect.tickedDown();
        })
        .filter(effect -> !effect.expired())
        .toList();

    effects.clear();
    effects.addAll(updated);
  }

  public void heal(int amount) {
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

  public int attackBonus() {
    return stats.modifier(STRENGTH);
  }

  public int spellBonus() {
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
    return armorClass + Optional.ofNullable(equippedArmor)
        .map(Item.Armor::armorBonus)
        .orElse(0);
  }

  public String name() {
    return name;
  }

  public DicePool damagePool() {
    return equippedWeapon != null ? equippedWeapon.damage() : damagePool;
  }

  public ActionStrategy strategy() {
    return strategy;
  }
}
