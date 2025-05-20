package fr.kevyn.smp.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CardItem extends OwnerableItem {
  public CardItem() {
    super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
  }
}
