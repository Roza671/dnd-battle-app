package com.dndbatleapp.application.battle;

import com.dndbatleapp.domain.creature.Creature;

import java.util.Map;
import java.util.Optional;

public record BattleStats(
    Map<Creature, Integer> damageByCreature,
    Optional<Creature> topDamageDealer,
    long criticalCount,
    long missCount,
    double averageActionsPerRound
) {
}
