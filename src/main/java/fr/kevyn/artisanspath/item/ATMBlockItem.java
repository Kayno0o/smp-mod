package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.init.ArtisansBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ATMBlockItem extends BlockItem {
  public ATMBlockItem() {
    super(ArtisansBlocks.ATM.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
  }
}
