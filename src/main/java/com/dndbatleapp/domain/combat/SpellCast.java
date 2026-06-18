package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record SpellCast(Creature caster, Creature target, int damage) implements ActionResult {
}