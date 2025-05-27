package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.component.AccountData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SmpComponents {
  public static final DeferredRegister.DataComponents REGISTRY =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SmpMod.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<AccountData>>
      OWNERSHIP = REGISTRY.registerComponentType("account", builder -> builder
      .persistent(AccountData.CODEC)
      .networkSynchronized(AccountData.STREAM_CODEC));
}
