package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;
import com.dndbatleapp.domain.dice.DicePool;

public record Heal(Creature healer, Creature target, DicePool amount) implements Action {
}