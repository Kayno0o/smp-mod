package fr.kevyn.artisanspath.event;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.ui.menu.BaseMenu;
import fr.kevyn.artisanspath.ui.screen.AccountsManagerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(
    modid = ArtisansMod.MODID,
    bus = EventBusSubscriber.Bus.GAME,
    value = Dist.CLIENT)
public class GameClientEvent {
  private GameClientEvent() {}

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    while (ModClientEvent.ACCOUNTS_MANAGER_MAPPING.get().consumeClick()) {
      Minecraft instance = Minecraft.getInstance();
      LocalPlayer player = instance.player;

      BaseMenu menu = new BaseMenu(0, player.getInventory());
      instance.setScreen(new AccountsManagerScreen(menu, player.getInventory()));
    }
  }
}
