package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.init.ArtisansBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ATMBlockItem extends BlockItem {
  public static final DeferredBlock<Block> BLOCK = ArtisansBlocks.ATM;

  public ATMBlockItem() {
    super(BLOCK.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
  }
}
