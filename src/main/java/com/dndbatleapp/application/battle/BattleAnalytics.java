package com.dndbatleapp.application.battle;

import com.dndbatleapp.domain.combat.ActionResult;
import com.dndbatleapp.domain.creature.Creature;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class BattleAnalytics {

  public BattleStats analyze(BattleOutcome outcome) {
    var actions = outcome.log();

    Map<Creature, Integer> damageByCreature = damageByCreature(actions);

    return new BattleStats(
        damageByCreature,
        topDamageDealer(damageByCreature),
        criticalCount(actions),
        missCount(actions),
        averageActionsPerRound(outcome)
    );
  }

  public Map<Creature, Integer> damageByCreature(List<ActionResult> actions) {
    return actions.stream()
        .collect(groupingBy(action -> switch (action) {
          case ActionResult.AttackHit attackHit -> attackHit.attacker();
          case ActionResult.AttackMiss attackMiss -> attackMiss.attacker();
          case ActionResult.Defended defended -> defended.self();
          case ActionResult.Healed healed -> healed.healer();
          case ActionResult.Skipped skipped -> skipped.self();
          case ActionResult.SpellCast spellCast -> spellCast.caster();
          case ActionResult.SpellMiss spellMiss -> spellMiss.caster();

        }, summingInt(action -> switch (action) {
          case ActionResult.AttackHit attackHit -> attackHit.damage();
          case ActionResult.SpellCast spellCast -> spellCast.damage();
          default -> 0;
        })));

  }

  public Optional<Creature> topDamageDealer(Map<Creature, Integer> damageByCreature) {
    return damageByCreature.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
  }

  public long criticalCount(List<ActionResult> actions) {
    return actions.stream()
        .filter(action -> switch (action) {
          case ActionResult.AttackHit attackHit -> attackHit.isCritical();
          case ActionResult.SpellCast spellCast -> spellCast.isCritical();
          default -> false;
        })
        .count();
  }

  public long missCount(List<ActionResult> actions) {
    return actions.stream()
        .filter(action -> action instanceof ActionResult.AttackMiss || action instanceof ActionResult.SpellMiss)
        .count();
  }

  public double averageActionsPerRound(BattleOutcome outcome) {
    if (outcome.rounds() == 0) {
      return 0;
    }

    return (double) outcome.log().size() / outcome.rounds();
  }
}
