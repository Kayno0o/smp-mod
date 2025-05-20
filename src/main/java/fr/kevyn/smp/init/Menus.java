package fr.kevyn.smp.init;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.ui.menu.ATMMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Menus {

  public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU,
      SmpMod.MODID);
  public static final DeferredHolder<MenuType<?>, MenuType<ATMMenu>> ATM_MENU = registerMenuType("atm_menu",
      ATMMenu::new);

  private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
      String name, IContainerFactory<T> factory) {
    return REGISTRY.register(name, () -> IMenuTypeExtension.create(factory));
  }
}
