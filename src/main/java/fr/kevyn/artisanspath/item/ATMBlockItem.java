package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.init.ArtisansBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ATMBlockItem extends BlockItem {
  public static final DeferredBlock<Block> BLOCK = ArtisansBlocks.ATM;
  public static final int DEFAULT_COLOR = 0xFFFFFF;

  public ATMBlockItem() {
    super(BLOCK.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
  }

  public static int getColor(ItemStack stack, int tintIndex) {
    if (tintIndex != 0) return -1;

    DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
    if (dyedColor != null) {
      return dyedColor.rgb();
    }

    return DEFAULT_COLOR;
  }
}
