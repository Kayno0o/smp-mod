package fr.kevyn.economy.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import fr.kevyn.economy.EconomyMod;

public class Tabs {
  public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
      EconomyMod.MODID);
  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ECONOMY_TAB = REGISTRY.register("economy_tab",
      () -> CreativeModeTab.builder().title(Component.translatable("item_group.economy.economy_tab"))
          .icon(() -> new ItemStack(Items.MONEY_10.get())).displayItems((parameters, tabData) -> {
            tabData.accept(Items.MONEY_1.get());
            tabData.accept(Items.MONEY_10.get());
            tabData.accept(Items.MONEY_100.get());
            tabData.accept(Items.MONEY_1000.get());
            tabData.accept(Items.CARD.get());
            tabData.accept(Blocks.ATM.get().asItem());
          }).build());
}
