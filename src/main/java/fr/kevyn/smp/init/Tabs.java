package fr.kevyn.smp.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import fr.kevyn.smp.SmpMod;
import net.minecraft.core.registries.Registries;

public class Tabs {
  public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
      SmpMod.MODID);
  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SMP_TAB = REGISTRY.register("smp_tab",
      () -> CreativeModeTab.builder().title(Component.translatable("item_group.smp.smp_tab"))
          .icon(() -> new ItemStack(Items.MONEY_10.get())).displayItems((parameters, tabData) -> {
            tabData.accept(Items.MONEY_1.get());
            tabData.accept(Items.MONEY_10.get());
            tabData.accept(Items.MONEY_100.get());
            tabData.accept(Items.MONEY_1000.get());
            tabData.accept(Items.CARD.get());
            tabData.accept(Blocks.ATM.get().asItem());
            tabData.accept(Blocks.REDSTONE_PAYGATE.get().asItem());
          }).build());
}
