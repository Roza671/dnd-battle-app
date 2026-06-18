package com.dndbatleapp.domain.combat.strategy;

import com.dndbatleapp.domain.combat.Action;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceRoller;
import com.dndbatleapp.domain.dice.DiceType;

import java.util.Comparator;
import java.util.Optional;

public class MageStrategy implements ActionStrategy {
  private static final int SPELL_MANA_COST = 5;

  @Override
  public Optional<Action> decide(Creature self, BattleState state, DiceRoller roller) {
    return state.enemiesOf(self).stream()
        .filter(Creature::isAlive)
        .min(Comparator.comparingInt(Creature::currentHp))
        .map(target -> {
          if (self.currentMana() >= SPELL_MANA_COST) {
            return new Action.CastDamageSpell(self, target, spellDamagePool(self), SPELL_MANA_COST);
          }

          return new Action.Attack(self, target);
        });
  }

  private DicePool spellDamagePool(Creature caster) {
    return new DicePool(1, DiceType.D10, caster.spellBonus());
  }
}
