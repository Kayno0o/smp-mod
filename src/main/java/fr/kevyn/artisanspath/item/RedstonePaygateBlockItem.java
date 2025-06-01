package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.init.ArtisansBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class RedstonePaygateBlockItem extends BlockItem {
  public static final DeferredBlock<Block> BLOCK = ArtisansBlocks.REDSTONE_PAYGATE;

  public RedstonePaygateBlockItem() {
    super(BLOCK.get(), new Item.Properties().stacksTo(16));
  }
}
