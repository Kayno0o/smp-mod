package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansTabs {
  private ArtisansTabs() {}

  public static final DeferredRegister<CreativeModeTab> REGISTRY =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArtisansMod.MODID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
      REGISTRY.register("tab", () -> CreativeModeTab.builder()
          .title(Component.translatable("item_group.artisanspath.tab"))
          .icon(() -> new ItemStack(ArtisansItems.PILE_OF_COINS.get()))
          .displayItems((parameters, tabData) -> {
            tabData.accept(ArtisansItems.COIN.get());
            tabData.accept(ArtisansItems.PILE_OF_COINS.get());
            tabData.accept(ArtisansItems.LARGE_PILE_OF_COINS.get());
            tabData.accept(ArtisansItems.COIN_INGOT.get());
            tabData.accept(ArtisansItems.PAYMENT_CARD.get());
            tabData.accept(ArtisansBlocks.ATM.get().asItem());
            tabData.accept(ArtisansBlocks.REDSTONE_PAYGATE.get().asItem());
          })
          .build());
}
