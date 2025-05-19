package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.atm.ATMBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Blocks {
  public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(SmpMod.MODID);
  public static final DeferredBlock<Block> ATM = REGISTRY.register("atm", ATMBlock::new);
}
