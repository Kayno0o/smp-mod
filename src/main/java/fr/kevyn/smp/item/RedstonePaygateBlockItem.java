package fr.kevyn.smp.item;

import fr.kevyn.smp.init.SmpBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class RedstonePaygateBlockItem extends BlockItem {
  public static DeferredBlock<Block> BLOCK = SmpBlocks.REDSTONE_PAYGATE;

  public RedstonePaygateBlockItem() {
    super(BLOCK.get(), new Item.Properties().stacksTo(16));
  }
}
