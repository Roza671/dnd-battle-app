package com.dndbatleapp.adapter.out.log;

import com.dndbatleapp.domain.combat.ActionResult;

public class BattleLogFormatter {

  public String describe(ActionResult result) {
    return switch (result) {
      case ActionResult.AttackHit(var attacker, var target, var damage, var isCritical) ->
          (isCritical ? "CRITICAL! " : "") + attacker.name() + " damaged " + target.name() + " for " + damage;
      case ActionResult.AttackMiss(var a, var t) -> a.name() + " missed on " + t.name();
      case ActionResult.Defended(var self) -> self.name() + " in defense";
      case ActionResult.Healed(var healer, var target, var amount) ->
          healer.name() + " healed the " + target.name() + " for " + amount;
      case ActionResult.Skipped(var self) -> self.name() + " skipped turn";
      case ActionResult.SpellCast(var caster, var target, var damage, var isCritical) ->
          (isCritical ? "CRITICAL! " : "") + caster.name() + " cast spell on the " + target.name() + " and deal the " + damage + " damage";
      case ActionResult.SpellMiss(var caster, var target) -> caster.name() + " missed spell on " + target.name();
    };
  }
}
