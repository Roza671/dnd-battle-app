package com.dndbatleapp.domain.combat.strategy;

import com.dndbatleapp.domain.combat.Action;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DiceRoller;

import java.util.Comparator;
import java.util.Optional;

public class WarriorStrategy implements ActionStrategy {
  @Override
  public Optional<Action> decide(Creature self, BattleState state, DiceRoller roller) {
    return state.enemiesOf(self).stream()
        .filter(Creature::isAlive)
        .min(Comparator.comparingInt(Creature::currentHp))
        .map(target -> new Action.Attack(self, target));
  }
}
