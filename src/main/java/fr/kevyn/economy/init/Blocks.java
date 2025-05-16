package fr.kevyn.economy.init;

import fr.kevyn.economy.EconomyMod;
import fr.kevyn.economy.block.ATMBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Blocks {
  public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(EconomyMod.MODID);
  public static final DeferredBlock<Block> ATM = REGISTRY.register("atm", ATMBlock::new);
}
