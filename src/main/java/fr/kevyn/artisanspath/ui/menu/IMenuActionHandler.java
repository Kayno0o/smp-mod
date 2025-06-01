package fr.kevyn.artisanspath.ui.menu;

import fr.kevyn.artisanspath.network.server.MenuActionPacket;

public interface IMenuActionHandler {
  public String getMenuIdentifier();

  public void handleMenuAction(MenuActionPacket action);
}
