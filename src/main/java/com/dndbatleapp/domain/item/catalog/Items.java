package com.dndbatleapp.domain.item.catalog;

import com.dndbatleapp.domain.dice.DicePool;
import com.dndbatleapp.domain.dice.DiceType;
import com.dndbatleapp.domain.item.Item;
import com.dndbatleapp.domain.item.PotionEffect;

public final class Items {

  private Items() {
  }

  public static Item.Weapon shortSword() {
    return new Item.Weapon("short_sword", "Short Sword",
        new DicePool(1, DiceType.D10, 0), 0);
  }

  public static Item.Weapon greatAxe() {
    return new Item.Weapon("great_axe", "Great Axe",
        new DicePool(1, DiceType.D12, 0), 1);
  }

  public static Item.Armor leatherArmor() {
    return new Item.Armor("leather", "Leather Armor", 2);
  }

  public static Item.Armor plateArmor() {
    return new Item.Armor("plate", "Plate Armor", 6);
  }

  public static Item.Potion healingPotion() {
    return new Item.Potion("heal_potion", "Healing Potion",
        PotionEffect.HEAL, 10);
  }
}