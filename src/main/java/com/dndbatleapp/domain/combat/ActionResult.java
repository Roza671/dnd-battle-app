package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public sealed interface ActionResult permits ActionResult.AttackHit, ActionResult.AttackMiss, ActionResult.Defended, ActionResult.Healed, ActionResult.Skipped, ActionResult.SpellCast, ActionResult.SpellMiss {
  record SpellMiss(Creature caster, Creature target) implements ActionResult {
  }

  record SpellCast(Creature caster, Creature target, int damage, boolean isCritical) implements ActionResult {
  }

  record Skipped(Creature self) implements ActionResult {
  }

  record Healed(Creature healer, Creature target, int amount) implements ActionResult {
  }

  record Defended(Creature self) implements ActionResult {
  }

  record AttackMiss(Creature attacker, Creature target) implements ActionResult {
  }

  record AttackHit(Creature attacker, Creature target, int damage, boolean isCritical) implements ActionResult {
  }
}
