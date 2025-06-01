package fr.kevyn.artisanspath.ui.menu;

import fr.kevyn.artisanspath.init.ArtisansMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class BaseMenu extends AbstractMenu<BaseMenu> {
  public BaseMenu(int containerId, Inventory inv, int slotsCount) {
    super(ArtisansMenus.BASE_MENU.get(), containerId, inv.player, slotsCount);
  }

  public BaseMenu(int containerId, Inventory inv) {
    this(containerId, inv, 0);
  }

  public BaseMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    this(containerId, playerInventory);
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }
}
