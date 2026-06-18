package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record Skip(Creature self) implements Action {
}