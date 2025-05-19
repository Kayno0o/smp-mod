package fr.kevyn.smp.init;

import java.util.function.Supplier;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.atm.ATMBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntities {
  public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister
      .create(Registries.BLOCK_ENTITY_TYPE, SmpMod.MODID);

  @SuppressWarnings("null")
  public static final Supplier<BlockEntityType<ATMBlockEntity>> ATM = REGISTRY.register(
      "atm",
      () -> BlockEntityType.Builder.of(
          ATMBlockEntity::new,
          Blocks.ATM.get())
          .build(null));
}
