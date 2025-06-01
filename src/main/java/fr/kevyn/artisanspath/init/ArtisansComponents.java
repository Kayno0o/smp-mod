package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.component.AccountData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansComponents {
  private ArtisansComponents() {}

  public static final DeferredRegister.DataComponents REGISTRY =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ArtisansMod.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<AccountData>>
      OWNERSHIP = REGISTRY.registerComponentType("account", builder -> builder
      .persistent(AccountData.CODEC)
      .networkSynchronized(AccountData.STREAM_CODEC));
}
