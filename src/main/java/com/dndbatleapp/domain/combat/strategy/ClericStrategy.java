package com.dndbatleapp.domain.combat.strategy;

import com.dndbatleapp.domain.combat.Action;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceType;

import java.util.Comparator;
import java.util.Optional;

public class ClericStrategy implements ActionStrategy {
  private static final int HEAL_MANA_COST = 5;

  @Override
  public Optional<Action> decide(Creature self, BattleState state) {
    return state.alliesOf(self).stream()
        .filter(Creature::isAlive)
        .min(Comparator.comparingInt(Creature::currentHp))
        .map(target -> {
          if (self.currentMana() >= HEAL_MANA_COST) {
            return new Action.Heal(self, target, spellHealPool(self), HEAL_MANA_COST);
          }

          return new Action.Attack(self, target);
        });
  }

  private DicePool spellHealPool(Creature caster) {
    return new DicePool(1, DiceType.D8, caster.spellBonus());
  }
}
