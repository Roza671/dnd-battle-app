package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record AttackHit(Creature attacker, Creature target, int damage, boolean critical) implements ActionResult {
}