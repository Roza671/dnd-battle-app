package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

public record AttackMiss(Creature attacker, Creature target) implements ActionResult {
}