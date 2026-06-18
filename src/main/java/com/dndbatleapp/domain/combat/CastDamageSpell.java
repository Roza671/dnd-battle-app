package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;

public record CastDamageSpell(Creature caster, Creature target, DicePool damage, int manaCost) implements Action {
}