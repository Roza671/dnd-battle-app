package com.dndbatleapp.adapter.out.log;

import com.dndbatleapp.application.battle.BattleOutcome;

public class ConsoleBattleReporter {
  private final BattleLogFormatter formatter;

  public ConsoleBattleReporter(BattleLogFormatter formatter) {
    this.formatter = formatter;
  }

  public void report(BattleOutcome outcome) {
    outcome.log().forEach(r -> System.out.println(formatter.describe(r)));

    System.out.println("Winner: " + outcome.winner() + " in " + outcome.rounds() + " rounds");
  }
}
