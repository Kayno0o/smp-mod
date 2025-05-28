package fr.kevyn.smp.ui.menu;

import fr.kevyn.smp.init.SmpMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AccountsManagerMenu extends AbstractMenu<AccountsManagerMenu> {
  public AccountsManagerMenu(int containerId, Inventory inv) {
    super(SmpMenus.ACCOUNTS_MANAGER_MENU.get(), containerId, inv.player, 0);
  }

  public AccountsManagerMenu(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    this(containerId, playerInventory);
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }
}
