package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.block.ATMBlockEntity;
import fr.kevyn.smp.block.RedstonePaygateBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SmpBlockEntities {
  private SmpBlockEntities() {}

  public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
      DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SmpMod.MODID);

  public static final Supplier<BlockEntityType<ATMBlockEntity>> ATM = REGISTRY.register(
      "atm",
      () -> BlockEntityType.Builder.of(ATMBlockEntity::new, SmpBlocks.ATM.get()).build(null));

  public static final Supplier<BlockEntityType<RedstonePaygateBlockEntity>> REDSTONE_PAYGATE =
      REGISTRY.register("redstone_paygate", () -> BlockEntityType.Builder.of(
              RedstonePaygateBlockEntity::new, SmpBlocks.REDSTONE_PAYGATE.get())
          .build(null));
}
