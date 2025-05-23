package fr.kevyn.smp.ui.menu;

import fr.kevyn.smp.network.server.MenuActionNet;

public interface IMenuNet {
  public String getMenuIdentifier();

  public void handleMenuAction(MenuActionNet action);
}
