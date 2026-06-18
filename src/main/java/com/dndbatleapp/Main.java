package com.dndbatleapp;

import com.dndbatleapp.adapter.out.log.BattleLogFormatter;
import com.dndbatleapp.adapter.out.log.ConsoleBattleReporter;
import com.dndbatleapp.adapter.out.random.DefaultRandom;
import com.dndbatleapp.application.battle.BattleAnalytics;
import com.dndbatleapp.application.battle.BattleService;
import com.dndbatleapp.domain.combat.ActionResolver;
import com.dndbatleapp.domain.combat.BattleState;
import com.dndbatleapp.domain.creature.bestiary.Creatures;
import com.dndbatleapp.domain.shared.Random;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    Random random = new DefaultRandom();
    var resolver = new ActionResolver();
    var service = new BattleService(resolver, random);
    var battleFormatter = new BattleLogFormatter();
    var analytics = new BattleAnalytics();
    ConsoleBattleReporter battleReporter = new ConsoleBattleReporter(battleFormatter);


    var heroes = List.of(Creatures.warrior("GUTS BERSERK"), Creatures.mage("Gandalf the Grey"));
    var enemies = List.of(Creatures.goblin(), Creatures.goblin());
    var state = new BattleState(heroes, enemies);

    var outcome = service.run(state);

    battleReporter.report(outcome);

    var stats = analytics.analyze(outcome);
    battleReporter.statistics(stats);
  }
}