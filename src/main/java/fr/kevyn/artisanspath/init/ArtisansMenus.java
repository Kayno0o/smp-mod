package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.ui.menu.ATMMenu;
import fr.kevyn.artisanspath.ui.menu.AccountSelectionMenu;
import fr.kevyn.artisanspath.ui.menu.BaseMenu;
import fr.kevyn.artisanspath.ui.menu.RedstonePaygateMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansMenus {
  private ArtisansMenus() {}

  public static final DeferredRegister<MenuType<?>> REGISTRY =
      DeferredRegister.create(Registries.MENU, ArtisansMod.MODID);

  public static final DeferredHolder<MenuType<?>, MenuType<ATMMenu>> ATM_MENU =
      registerMenuType("atm_menu", ATMMenu::new);

  public static final DeferredHolder<MenuType<?>, MenuType<RedstonePaygateMenu>>
      REDSTONE_PAYGATE_MENU = registerMenuType("redstone_paygate_menu", RedstonePaygateMenu::new);

  public static final DeferredHolder<MenuType<?>, MenuType<AccountSelectionMenu>>
      ACCOUNT_SELECTION_MENU =
          registerMenuType("account_selection_menu", AccountSelectionMenu::new);

  public static final DeferredHolder<MenuType<?>, MenuType<BaseMenu>> BASE_MENU =
      registerMenuType("accounts_manager_menu", BaseMenu::new);

  private static <T extends AbstractContainerMenu>
      DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
          String name, IContainerFactory<T> factory) {
    return REGISTRY.register(name, () -> IMenuTypeExtension.create(factory));
  }
}
