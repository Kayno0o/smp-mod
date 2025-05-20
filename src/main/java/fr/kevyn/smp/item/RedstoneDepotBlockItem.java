package fr.kevyn.smp.item;

import fr.kevyn.smp.init.Blocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class RedstoneDepotBlockItem extends BlockItem {
  public static DeferredBlock<Block> BLOCK = Blocks.REDSTONE_DEPOT;

  public RedstoneDepotBlockItem() {
    super(BLOCK.get(), new Item.Properties().stacksTo(16));
  }
}
