package com.dndbatleapp.adapter.out.log;

import com.dndbatleapp.application.battle.BattleOutcome;
import com.dndbatleapp.application.battle.BattleStats;
import com.dndbatleapp.domain.creature.Creature;

public class ConsoleBattleReporter {
  private final BattleLogFormatter formatter;

  public ConsoleBattleReporter(BattleLogFormatter formatter) {
    this.formatter = formatter;
  }

  public void report(BattleOutcome outcome) {
    outcome.log().forEach(r -> System.out.println(formatter.describe(r)));
    System.out.println("============== ROUND LOG ==============");

    System.out.println("Winner: " + outcome.winner() + " in " + outcome.rounds() + " rounds");
  }

  public void statistics(BattleStats stats) {
    System.out.println("============== ROUND STATISTICS ==============");

    System.out.println("Average actions per round: " + stats.averageActionsPerRound());
    System.out.println("Total critical damage: " + stats.criticalCount());
    System.out.println("Total missed hits: " + stats.missCount());

    System.out.println("============== DAMAGE STATISTIC ==============");

    stats.damageByCreature().forEach((creature, damage) -> {
      System.out.printf("Creature %s dealing the %d damage.%n", creature.name(), damage);
    });

    String best = stats.topDamageDealer()
        .map(Creature::name)
        .orElse("Unknown");

    System.out.println("Best DPS guy: " + best);
  }
}
