package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record Defend(Creature self) implements Action {
}