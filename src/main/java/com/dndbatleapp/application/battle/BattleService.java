package com.dndbatleapp.application.battle;

import com.dndbatleapp.domain.combat.ActionResolver;
import com.dndbatleapp.domain.combat.ActionResult;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DiceRoller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class BattleService {
  private static final int MAX_ROUNDS = 50;

  private final ActionResolver resolver;
  private final DiceRoller roller;

  public BattleService(ActionResolver resolver, DiceRoller roller) {
    this.resolver = resolver;
    this.roller = roller;
  }

  public BattleOutcome run(BattleState state) {
    List<ActionResult> log = new ArrayList<>();
    List<Creature> order = rollInitiative(state, roller);

    int round = 0;

    while (!state.isOver() && round < MAX_ROUNDS) {
      round++;
      for (Creature c : order) {
        if (!c.isAlive()) {
          continue;
        }

        c.tickEffects();

        if (c.isStunned()) {
          continue;
        }

        c.strategy().decide(c, state, roller)
            .map(action -> resolver.resolve(action, roller))
            .ifPresent(log::add);

        if (state.isOver()) {
          break;
        }
      }
    }

    return new BattleOutcome(state.winner().orElse(null), round, log);
  }

  private List<Creature> rollInitiative(BattleState state, DiceRoller roller) {
    return Stream.concat(state.aliveHeroes().stream(), state.aliveEnemies().stream())
        .sorted(Comparator.comparingInt(
            (Creature c) -> roller.roll(20) + c.initiativeBonus()).reversed())
        .toList();
  }
}