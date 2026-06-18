package com.dndbatleapp.domain.combat;

import com.dndbatleapp.domain.creature.Creature;

import java.util.List;
import java.util.Optional;

public final class BattleState {
  private final List<Creature> heroes;
  private final List<Creature> enemies;

  public BattleState(List<Creature> heroes, List<Creature> enemies) {
    this.heroes = heroes;
    this.enemies = enemies;
  }

  public List<Creature> aliveHeroes() {
    return heroes.stream()
        .filter(Creature::isAlive)
        .toList();
  }

  public List<Creature> aliveEnemies() {
    return enemies.stream()
        .filter(Creature::isAlive)
        .toList();
  }

  public List<Creature> alliesOf(Creature c) {
    return heroes.contains(c) ? heroes : enemies;
  }

  public List<Creature> enemiesOf(Creature c) {
    return heroes.contains(c) ? enemies : heroes;
  }

  public boolean isOver() {
    return aliveHeroes().isEmpty() || aliveEnemies().isEmpty();
  }

  public Optional<Side> winner() {
    if (!isOver()) {
      return Optional.empty();
    }

    return aliveHeroes().isEmpty() ? Optional.of(Side.ENEMIES) : Optional.of(Side.HEROES);
  }
}
