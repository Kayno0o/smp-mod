package fr.kevyn.artisanspath.ui.menu;

import fr.kevyn.artisanspath.init.ArtisansMenus;
import fr.kevyn.artisanspath.item.PaymentCardItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AccountSelectionMenu extends AbstractMenu<AccountSelectionMenu> {
  public AccountSelectionMenu(int containerId, Inventory inv) {
    super(ArtisansMenus.ACCOUNT_SELECTION_MENU.get(), containerId, inv.player, 0);
  }

  public AccountSelectionMenu(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    this(containerId, playerInventory);
  }

  @Override
  public boolean stillValid(Player player) {
    return player.getMainHandItem().getItem() instanceof PaymentCardItem;
  }
}
