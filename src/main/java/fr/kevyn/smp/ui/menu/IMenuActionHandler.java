package fr.kevyn.smp.ui.menu;

import fr.kevyn.smp.network.server.MenuActionPacket;

public interface IMenuActionHandler {
  public String getMenuIdentifier();

  public void handleMenuAction(MenuActionPacket action);
}
