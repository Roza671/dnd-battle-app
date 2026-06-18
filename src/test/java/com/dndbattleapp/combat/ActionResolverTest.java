package com.dndbattleapp.combat;

import com.dndbatleapp.domain.combat.Action;
import com.dndbatleapp.domain.combat.ActionResolver;
import com.dndbatleapp.domain.combat.ActionResult;
import com.dndbatleapp.domain.creature.bestiary.Creatures;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceRoller;
import com.dndbatleapp.domain.dice.DiceType;
import com.dndbatleapp.domain.exception.DeadCreatureException;
import com.dndbatleapp.domain.exception.NotEnoughManaException;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class ActionResolverTest {

  private final ActionResolver resolver = new ActionResolver();

  private static DiceRoller fixed(int... values) {
    Queue<Integer> queue = new ArrayDeque<>();
    for (int v : values) {
      queue.add(v);
    }
    return sides -> {
      if (queue.isEmpty()) {
        throw new IllegalStateException("No more preset rolls");
      }
      return queue.poll();
    };
  }

  private static DiceRoller always(int value) {
    return sides -> value;
  }

  // ---- ATTACK -----------------------------------------------------------

  @Test
  void naturalTwentyIsCriticalHit() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.goblin();

    var result = resolver.resolve(new Action.Attack(attacker, target), fixed(20, 5, 6));

    assertInstanceOf(ActionResult.AttackHit.class, result);
    var hit = (ActionResult.AttackHit) result;
    assertTrue(hit.critical(), "натуральная 20 должна быть критом");
  }

  @Test
  void naturalOneAlwaysMisses() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.goblin();

    var result = resolver.resolve(new Action.Attack(attacker, target), always(1));

    assertInstanceOf(ActionResult.AttackMiss.class, result);
  }

  @Test
  void rollBelowArmorClassMisses() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.warrior("Tank");

    var result = resolver.resolve(new Action.Attack(attacker, target), always(2));

    assertInstanceOf(ActionResult.AttackMiss.class, result);
  }

  @Test
  void rollMeetingArmorClassHits() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.goblin();

    var result = resolver.resolve(new Action.Attack(attacker, target), fixed(10, 1, 1));

    assertInstanceOf(ActionResult.AttackHit.class, result);
    var hit = (ActionResult.AttackHit) result;
    assertFalse(hit.critical());
    assertEquals(2, hit.damage());
  }

  @Test
  void attackHitReducesTargetHp() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.goblin();
    int before = target.currentHp();

    resolver.resolve(new Action.Attack(attacker, target), fixed(15, 3, 4));

    assertTrue(target.currentHp() < before, "цель должна получить урон");
    assertEquals(before - 7, target.currentHp());
  }

  @Test
  void criticalDoublesDiceNotModifier() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.dragon();
    int before = target.currentHp();

    resolver.resolve(new Action.Attack(attacker, target), fixed(20, 4, 5));

    assertEquals(before - 18, target.currentHp());
  }

  @Test
  void attackingDeadCreatureThrows() {
    var attacker = Creatures.warrior("Hero");
    var target = Creatures.goblin();
    target.takeDamage(9999);

    assertThrows(DeadCreatureException.class,
        () -> resolver.resolve(new Action.Attack(attacker, target), always(15)));
  }

  // ---- HEAL -------------------------------------------------------------

  @Test
  void healRestoresHp() {
    var healer = Creatures.mage("Cleric");
    var target = Creatures.warrior("Hero");
    target.takeDamage(10);
    int before = target.currentHp();
    int manaCost = 5;

    var pool = new DicePool(2, DiceType.D6, 0);
    var result = resolver.resolve(new Action.Heal(healer, target, pool, manaCost), fixed(3, 2));

    assertInstanceOf(ActionResult.Healed.class, result);
    assertEquals(5, ((ActionResult.Healed) result).amount());
    assertEquals(before + 5, target.currentHp());
  }

  @Test
  void healingDeadCreatureThrows() {
    var healer = Creatures.mage("Cleric");
    var target = Creatures.goblin();
    target.takeDamage(9999);
    int manaCost = 5;

    var pool = new DicePool(1, DiceType.D6, 0);
    assertThrows(DeadCreatureException.class,
        () -> resolver.resolve(new Action.Heal(healer, target, pool, manaCost), always(4)));
  }

  // ---- DEFEND / SKIP ----------------------------------------------------

  @Test
  void defendReturnsDefended() {
    var self = Creatures.warrior("Hero");
    var result = resolver.resolve(new Action.Defend(self), always(1));
    assertInstanceOf(ActionResult.Defended.class, result);
  }

  @Test
  void skipReturnsSkipped() {
    var self = Creatures.warrior("Hero");
    var result = resolver.resolve(new Action.Skip(self), always(1));
    assertInstanceOf(ActionResult.Skipped.class, result);
  }

  // ---- SPELL ------------------------------------------------------------

  @Test
  void spellHitReturnsSpellCast() {
    var caster = Creatures.mage("Mage");
    var target = Creatures.goblin();

    var spell = new Action.CastDamageSpell(caster, target, new DicePool(2, DiceType.D6, 0), 5);
    var result = resolver.resolve(spell, fixed(10, 4, 3));

    assertInstanceOf(ActionResult.SpellCast.class, result);
    assertEquals(7, ((ActionResult.SpellCast) result).damage());
  }

  @Test
  void spellConsumesMana() {
    var caster = Creatures.mage("Mage");
    var target = Creatures.goblin();
    int manaBefore = caster.currentMana();

    var spell = new Action.CastDamageSpell(caster, target, new DicePool(1, DiceType.D6, 0), 5);
    resolver.resolve(spell, fixed(15, 4));

    assertEquals(manaBefore - 5, caster.currentMana(), "мана должна списаться");
  }

  @Test
  void spellWithoutEnoughManaThrows() {
    var caster = Creatures.warrior("Hero");
    var target = Creatures.goblin();

    var spell = new Action.CastDamageSpell(caster, target, new DicePool(1, DiceType.D6, 0), 5);
    assertThrows(NotEnoughManaException.class,
        () -> resolver.resolve(spell, always(15)));
  }

  @Test
  void spellOnDeadTargetThrows() {
    var caster = Creatures.mage("Mage");
    var target = Creatures.goblin();
    target.takeDamage(9999);

    var spell = new Action.CastDamageSpell(caster, target, new DicePool(1, DiceType.D6, 0), 5);
    assertThrows(DeadCreatureException.class,
        () -> resolver.resolve(spell, always(15)));
  }
}