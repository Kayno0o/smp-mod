package fr.kevyn.smp.item;

import fr.kevyn.smp.init.Blocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ATMBlockItem extends BlockItem {
  public static DeferredBlock<Block> BLOCK = Blocks.ATM;

  public ATMBlockItem() {
    super(BLOCK.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
  }
}
