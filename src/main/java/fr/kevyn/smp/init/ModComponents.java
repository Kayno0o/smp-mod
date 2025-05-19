package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.component.OwnershipData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModComponents {
  public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister
      .createDataComponents(Registries.DATA_COMPONENT_TYPE, SmpMod.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<OwnershipData>> OWNERSHIP = REGISTRY
      .registerComponentType(
          "ownership",
          builder -> builder
              .persistent(OwnershipData.CODEC)
              .networkSynchronized(OwnershipData.STREAM_CODEC));
}
