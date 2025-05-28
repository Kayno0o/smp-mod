package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SmpTabs {
  private SmpTabs() {}

  public static final DeferredRegister<CreativeModeTab> REGISTRY =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SmpMod.MODID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SMP_TAB =
      REGISTRY.register("smp_tab", () -> CreativeModeTab.builder()
          .title(Component.translatable("item_group.smp.smp_tab"))
          .icon(() -> new ItemStack(SmpItems.SMALL_BILL.get()))
          .displayItems((parameters, tabData) -> {
            tabData.accept(SmpItems.COIN.get());
            tabData.accept(SmpItems.SMALL_BILL.get());
            tabData.accept(SmpItems.BIG_BILL.get());
            tabData.accept(SmpItems.MONEY_INGOT.get());
            tabData.accept(SmpItems.CARD.get());
            tabData.accept(SmpBlocks.ATM.get().asItem());
            tabData.accept(SmpBlocks.REDSTONE_PAYGATE.get().asItem());
          })
          .build());
}
