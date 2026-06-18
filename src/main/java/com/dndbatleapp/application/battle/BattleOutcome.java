package com.dndbatleapp.application.battle;

import com.dndbatleapp.domain.combat.ActionResult;
import com.dndbatleapp.domain.combat.Side;

import java.util.List;

public record BattleOutcome(Side winner, int rounds, List<ActionResult> log) {
  public BattleOutcome {
    log = List.copyOf(log);
  }
}
