package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.block.ATMBlockEntity;
import fr.kevyn.artisanspath.block.RedstonePaygateBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansBlockEntities {
  private ArtisansBlockEntities() {}

  public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
      DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ArtisansMod.MODID);

  public static final Supplier<BlockEntityType<ATMBlockEntity>> ATM = REGISTRY.register(
      "atm", () -> BlockEntityType.Builder.of(ATMBlockEntity::new, ArtisansBlocks.ATM.get())
          .build(null));

  public static final Supplier<BlockEntityType<RedstonePaygateBlockEntity>> REDSTONE_PAYGATE =
      REGISTRY.register("redstone_paygate", () -> BlockEntityType.Builder.of(
              RedstonePaygateBlockEntity::new, ArtisansBlocks.REDSTONE_PAYGATE.get())
          .build(null));
}
