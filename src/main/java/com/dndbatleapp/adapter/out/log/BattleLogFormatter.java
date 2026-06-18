package com.dndbatleapp.adapter.out.log;

import com.dndbatleapp.domain.combat.*;

public class BattleLogFormatter {

  public String describe(ActionResult result) {
    return switch (result) {
      case AttackHit(var a, var t, var dmg, var crit) ->
          (crit ? "CRITICAL! " : "") + a.name() + " damaged " + t.name() + " for " + dmg;
      case AttackMiss(var a, var t) -> a.name() + " missed on " + t.name();
      case Defended(var self) -> self.name() + " in defense";
      case Healed(var healer, var target, var amount) ->
          healer.name() + " healed the " + target.name() + " for " + amount;
      case Skipped(var self) -> self.name() + " skipped turn";
      case SpellCast(var caster, var target, var damage) ->
          caster.name() + " cast spell on the " + target.name() + " and deal the " + damage + " damage";
    };
  }
}
