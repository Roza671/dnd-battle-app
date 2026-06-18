package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record Attack(Creature attacker, Creature target) implements Action {
}
