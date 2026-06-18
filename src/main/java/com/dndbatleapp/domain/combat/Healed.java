package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record Healed(Creature healer, Creature target, int amount) implements ActionResult {
}