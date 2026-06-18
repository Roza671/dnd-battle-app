package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record Defended(Creature self) implements ActionResult {

}