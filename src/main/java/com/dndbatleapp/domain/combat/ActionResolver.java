package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.RollResult;
import com.dndbatleapp.domain.exception.DeadCreatureException;
import com.dndbatleapp.domain.exception.NotEnoughManaException;
import com.dndbatleapp.domain.shared.Random;

public class ActionResolver {
  public ActionResult resolve(Action action, Random random) {
    return switch (action) {
      case Action.Attack(var attacker, var target) -> resolveAttack(attacker, target, random);
      case Action.Heal heal -> resolveHeal(heal, random);
      case Action.CastDamageSpell c -> resolveSpell(c, random);
      case Action.Defend(var self) -> resolveDefend(self);
      case Action.Skip(var self) -> resolveSkip(self);
    };
  }

  private ActionResult resolveAttack(Creature attacker, Creature target, Random random) {
    if (!target.isAlive()) {
      throw new DeadCreatureException(
          "Cannot attack a dead creature: " + target.name());
    }

    int attackRoll = random.next(20);

    if (attackRoll == 1) {
      return new ActionResult.AttackMiss(attacker, target);
    }

    boolean isCritical = attackRoll == 20;

    if (!isCritical && (attackRoll + attacker.attackBonus()) < target.armorClass()) {
      return new ActionResult.AttackMiss(attacker, target);
    }

    int damage = rollDamage(attacker, random, isCritical);

    target.takeDamage(damage);

    return new ActionResult.AttackHit(attacker, target, damage, isCritical);
  }

  private int rollDamage(Creature attacker, Random random, boolean critical) {
    DicePool weaponDamage = attacker.damagePool();
    RollResult result = weaponDamage.roll(random);
    int damage = result.total();

    if (critical) {
      int diceOnly = result.total() - result.modifier();
      damage += diceOnly;
    }

    return Math.max(0, damage);
  }

  private ActionResult resolveSpell(Action.CastDamageSpell spell, Random random) {
    if (!spell.target().isAlive()) {
      throw new DeadCreatureException(
          "Cannot attack a dead creature: " + spell.target().name());
    }

    if (spell.manaCost() > spell.caster().currentMana()) {
      throw new NotEnoughManaException(
          String.format("Dont enough mana for the spell. Required %d but have %d", spell.manaCost(), spell.caster().currentMana())
      );
    }

    spell.caster().takeMana(spell.manaCost());

    int attackRoll = random.next(20);

    if (attackRoll == 1) {
      return new ActionResult.SpellMiss(spell.caster(), spell.target());
    }


    boolean isCritical = attackRoll == 20;

    if (!isCritical && (attackRoll + spell.caster().spellBonus()) < spell.target().armorClass()) {
      return new ActionResult.SpellMiss(spell.caster(), spell.target());
    }

    int damage = rollSpellDamage(spell, random, isCritical);
    spell.target().takeDamage(damage);

    return new ActionResult.SpellCast(spell.caster(), spell.target(), damage, isCritical);
  }

  private int rollSpellDamage(Action.CastDamageSpell castSpell, Random random, boolean critical) {
    DicePool spellDamage = castSpell.damage();
    RollResult result = spellDamage.roll(random);
    int damage = result.total();

    if (critical) {
      int diceOnly = result.total() - result.modifier();
      damage += diceOnly;
    }

    return Math.max(0, damage);
  }

  private ActionResult resolveHeal(Action.Heal healSpell, Random random) {
    if (!healSpell.target().isAlive()) {
      throw new DeadCreatureException("Cannot heal a dead creature: " + healSpell.target().name());
    }

    int amount = healSpell.pool().roll(random).total();
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
