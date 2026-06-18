package com.dndbatleapp.domain.combat.strategy;

import com.dndbatleapp.domain.combat.Action;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DiceRoller;

import java.util.List;
import java.util.Optional;

public class MonsterStrategy implements ActionStrategy {
  @Override
  public Optional<Action> decide(Creature self, BattleState state, DiceRoller roller) {
    List<Creature> aliveEnemies = state.enemiesOf(self).stream()
        .filter(Creature::isAlive)
        .toList();

    if (aliveEnemies.isEmpty()) {
      return Optional.empty();
    }

    int potentialTargetId = roller.roll(aliveEnemies.size()) - 1;
    Creature target = aliveEnemies.get(potentialTargetId);

    return Optional.of(new Action.Attack(self, target));
  }
}
