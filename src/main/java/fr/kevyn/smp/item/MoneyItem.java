package fr.kevyn.smp.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class MoneyItem extends Item {
  private final int value;

  public MoneyItem(int value) {
    super(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
