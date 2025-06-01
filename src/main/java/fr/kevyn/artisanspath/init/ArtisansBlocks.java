package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.block.ATMBlock;
import fr.kevyn.artisanspath.block.RedstonePaygateBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansBlocks {
  private ArtisansBlocks() {}

  public static final DeferredRegister.Blocks REGISTRY =
      DeferredRegister.createBlocks(ArtisansMod.MODID);

  public static final DeferredBlock<Block> ATM =
      REGISTRY.register("atm", registry -> new ATMBlock());
  public static final DeferredBlock<Block> REDSTONE_PAYGATE =
      REGISTRY.register("redstone_paygate", registry -> new RedstonePaygateBlock());
}
