package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;

public sealed interface Action permits Action.Attack, Action.CastDamageSpell, Action.Defend, Action.Heal, Action.Skip {
  record Skip(Creature self) implements Action {
  }

  record Heal(Creature healer, Creature target, DicePool amount) implements Action {
  }

  record Defend(Creature self) implements Action {
  }

  record CastDamageSpell(Creature caster, Creature target, DicePool damage, int manaCost) implements Action {
  }

  record Attack(Creature attacker, Creature target) implements Action {
  }
}
