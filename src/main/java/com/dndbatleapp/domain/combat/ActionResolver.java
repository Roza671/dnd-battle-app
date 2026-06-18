package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceRoller;
import com.dndbatleapp.domain.dice.RollResult;
import com.dndbatleapp.domain.exception.DeadCreatureException;
import com.dndbatleapp.domain.exception.NotEnoughManaException;

public class ActionResolver {

  public ActionResult resolve(Action action, DiceRoller roller) {
    return switch (action) {
      case Attack(var attacker, var target) -> resolveAttack(attacker, target, roller);
      case Heal(var healer, var target, var pool) -> resolveHeal(healer, target, pool, roller);
      case CastDamageSpell c -> resolveSpell(c, roller);
      case Defend(var self) -> resolveDefend(self);
      case Skip(var self) -> resolveSkip(self);
    };
  }

  private ActionResult resolveAttack(Creature attacker, Creature target, DiceRoller roller) {
    if (!target.isAlive()) {
      throw new DeadCreatureException(
          "Cannot attack a dead creature: " + target.name());
    }

    int attackRoll = roller.roll(20);

    if (attackRoll == 1) {
      return new AttackMiss(attacker, target);
    }

    boolean isCritical = attackRoll == 20;

    if (!isCritical && (attackRoll + attacker.attackBonus()) < target.armorClass()) {
      return new AttackMiss(attacker, target);
    }

    int damage = rollDamage(attacker, roller, isCritical);

    target.takeDamage(damage);

    return new AttackHit(attacker, target, damage, isCritical);
  }

  private int rollDamage(Creature attacker, DiceRoller roller, boolean critical) {
    DicePool weaponDamage = attacker.damagePool();
    RollResult result = weaponDamage.roll(roller);
    int damage = result.total();

    if (critical) {
      int diceOnly = result.total() - result.modifier();
      damage += diceOnly;
    }

    return Math.max(0, damage);
  }

  private ActionResult resolveSpell(CastDamageSpell c, DiceRoller roller) {
    if (!c.target().isAlive()) {
      throw new DeadCreatureException(
          "Cannot attack a dead creature: " + c.target().name());
    }

    if (c.manaCost() > c.caster().currentMana()) {
      throw new NotEnoughManaException(
          String.format("Dont enough mana for the spell. Required %d but have %d", c.manaCost(), c.caster().currentMana())
      );
    }

    c.caster().takeMana(c.manaCost());

    int attackRoll = roller.roll(20);

    if (attackRoll == 1) {
      return new SpellMiss(c.caster(), c.target());
    }


    boolean isCritical = attackRoll == 20;

    if (!isCritical && (attackRoll + c.caster().spellBonus()) < c.target().armorClass()) {
      return new SpellMiss(c.caster(), c.target());
    }

    int damage = rollSpellDamage(c, roller, isCritical);
    c.target().takeDamage(damage);

    return new SpellCast(c.caster(), c.target(), damage, isCritical);
  }

  private int rollSpellDamage(CastDamageSpell c, DiceRoller roller, boolean critical) {
    DicePool spellDamage = c.damage();
    RollResult result = spellDamage.roll(roller);
    int damage = result.total();

    if (critical) {
      int diceOnly = result.total() - result.modifier();
      damage += diceOnly;
    }

    return Math.max(0, damage);
  }

  private ActionResult resolveHeal(Creature healer, Creature target, DicePool pool, DiceRoller roller) {
    if (!target.isAlive()) {
      throw new DeadCreatureException("Cannot heal a dead creature: " + target.name());
    }

    int amount = pool.roll(roller).total();
    target.heal(amount);

    return new Healed(healer, target, amount);
  }

  private ActionResult resolveDefend(Creature self) {
    return new Defended(self);
  }

  private ActionResult resolveSkip(Creature self) {
    return new Skipped(self);
  }

}
