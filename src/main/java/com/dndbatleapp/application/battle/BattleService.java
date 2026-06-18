package com.dndbatleapp.application.battle;

import com.dndbatleapp.domain.combat.ActionResolver;
import com.dndbatleapp.domain.combat.ActionResult;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.shared.Random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class BattleService {
  private static final int MAX_ROUNDS = 50;

  private final ActionResolver resolver;
  private final Random random;

  public BattleService(ActionResolver resolver, Random random) {
    this.resolver = resolver;
    this.random = random;
  }

  public BattleOutcome run(BattleState state) {
    List<ActionResult> log = new ArrayList<>();
    List<Creature> order = rollInitiative(state, random);

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

        c.strategy().decide(c, state)
            .map(action -> resolver.resolve(action, random))
            .ifPresent(log::add);

        if (state.isOver()) {
          break;
        }
      }
    }

    return new BattleOutcome(state.winner().orElse(null), round, log);
  }

  private List<Creature> rollInitiative(BattleState state, Random random) {
    return Stream.concat(state.aliveHeroes().stream(), state.aliveEnemies().stream())
        .sorted(Comparator.comparingInt((Creature c) -> random.next(20) + c.initiativeBonus()).reversed())
        .toList();
  }
}