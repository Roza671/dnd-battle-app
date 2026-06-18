package com.dndbatleapp.domain.item;

import com.dndbatleapp.domain.shared.Registry;

import java.util.Comparator;
import java.util.Optional;

public class Inventory {
  private final Registry<String, Item> items = new Registry<>();

  public void add(Item item) {
    items.add(item);
  }

  public Optional<Item.Weapon> bestWeapon() {
    return items.findBy(i -> i instanceof Item.Weapon).stream()
        .map(i -> (Item.Weapon) i)
        .max(Comparator.comparingInt(w -> w.damage().count()));
  }

  public Optional<Item.Armor> bestArmor() {
    return items.findBy(i -> i instanceof Item.Armor).stream()
        .map(i -> (Item.Armor) i)
        .max(Comparator.comparingInt(Item.Armor::armorBonus));
  }

  public Optional<Item.Potion> firstPotion(PotionEffect potionEffect) {
    return items.findFirst(i -> i instanceof Item.Potion).stream()
        .map(i -> (Item.Potion) i)
        .findFirst();
  }

}
