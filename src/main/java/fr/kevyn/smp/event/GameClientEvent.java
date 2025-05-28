package fr.kevyn.smp.event;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.ui.menu.AccountsManagerMenu;
import fr.kevyn.smp.ui.screen.AccountsManagerScreen;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = SmpMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameClientEvent {
  private GameClientEvent() {}

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    while (ModClientEvent.ACCOUNTS_MANAGER_MAPPING.get().consumeClick()) {
      Minecraft instance = Minecraft.getInstance();
      LocalPlayer player = instance.player;

      var accounts = player.getData(SmpDataAttachments.LOCAL_ACCOUNTS);
      AccountsManagerMenu menu = new AccountsManagerMenu(0, player.getInventory());
      instance.setScreen(new AccountsManagerScreen(
          menu,
          player.getInventory(),
          Component.literal("Accounts management"),
          new ArrayList<>(accounts.values())));
    }
  }
}
