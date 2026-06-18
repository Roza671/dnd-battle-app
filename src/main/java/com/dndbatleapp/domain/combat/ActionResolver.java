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
      case Action.Attack(var attacker, var target) -> resolveAttack(attacker, target, roller);
      case Action.Heal heal -> resolveHeal(heal, roller);
      case Action.CastDamageSpell c -> resolveSpell(c, roller);
      case Action.Defend(var self) -> resolveDefend(self);
      case Action.Skip(var self) -> resolveSkip(self);
    };
  }

  private ActionResult resolveAttack(Creature attacker, Creature target, DiceRoller roller) {
    if (!target.isAlive()) {
      throw new DeadCreatureException(
          "Cannot attack a dead creature: " + target.name());
    }

    int attackRoll = roller.roll(20);

    if (attackRoll == 1) {
      return new ActionResult.AttackMiss(attacker, target);
    }

    boolean isCritical = attackRoll == 20;

    if (!isCritical && (attackRoll + attacker.attackBonus()) < target.armorClass()) {
      return new ActionResult.AttackMiss(attacker, target);
    }

    int damage = rollDamage(attacker, roller, isCritical);

    target.takeDamage(damage);

    return new ActionResult.AttackHit(attacker, target, damage, isCritical);
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

  private ActionResult resolveSpell(Action.CastDamageSpell c, DiceRoller roller) {
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
      return new ActionResult.SpellMiss(c.caster(), c.target());
    }


    boolean isCritical = attackRoll == 20;

    if (!isCritical && (attackRoll + c.caster().spellBonus()) < c.target().armorClass()) {
      return new ActionResult.SpellMiss(c.caster(), c.target());
    }

    int damage = rollSpellDamage(c, roller, isCritical);
    c.target().takeDamage(damage);

    return new ActionResult.SpellCast(c.caster(), c.target(), damage, isCritical);
  }

  private int rollSpellDamage(Action.CastDamageSpell castSpell, DiceRoller roller, boolean critical) {
    DicePool spellDamage = castSpell.damage();
    RollResult result = spellDamage.roll(roller);
    int damage = result.total();

    if (critical) {
      int diceOnly = result.total() - result.modifier();
      damage += diceOnly;
    }

    return Math.max(0, damage);
  }

  private ActionResult resolveHeal(Action.Heal healSpell, DiceRoller roller) {
    if (!healSpell.target().isAlive()) {
      throw new DeadCreatureException("Cannot heal a dead creature: " + healSpell.target().name());
    }

    int amount = healSpell.pool().roll(roller).total();
    healSpell.target().heal(amount);
    healSpell.healer().takeMana(healSpell.manaCost());

    return new ActionResult.Healed(healSpell.healer(), healSpell.target(), amount);
  }

  private ActionResult resolveDefend(Creature self) {
    return new ActionResult.Defended(self);
  }

  private ActionResult resolveSkip(Creature self) {
    return new ActionResult.Skipped(self);
  }

}
