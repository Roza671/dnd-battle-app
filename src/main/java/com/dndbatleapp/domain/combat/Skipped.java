package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record Skipped(Creature self) implements ActionResult {
}