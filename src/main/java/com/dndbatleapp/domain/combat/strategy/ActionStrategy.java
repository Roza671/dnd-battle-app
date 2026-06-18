package com.dndbatleapp.domain.combat.strategy;

import com.dndbatleapp.domain.combat.Action;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;

import java.util.Optional;

@FunctionalInterface
public interface ActionStrategy {
  Optional<Action> decide(Creature self, BattleState state);
}