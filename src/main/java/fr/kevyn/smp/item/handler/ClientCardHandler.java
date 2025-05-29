package fr.kevyn.smp.item.handler;

import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.ui.menu.AccountSelectionMenu;
import fr.kevyn.smp.ui.screen.AccountSelectionScreen;
import fr.kevyn.smp.utils.AccountUtils;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientCardHandler {
  private ClientCardHandler() {}

  public static void openSelectionScreen(LocalPlayer localPlayer, InteractionHand usedHand) {
    var accounts = localPlayer.getData(SmpDataAttachments.ACCOUNTS);

    UUID currentAccount = AccountUtils.getAccountUUID(localPlayer.getItemInHand(usedHand));

    AccountSelectionMenu menu = new AccountSelectionMenu(0, localPlayer.getInventory());

    Minecraft.getInstance()
        .setScreen(new AccountSelectionScreen(
            menu,
            localPlayer.getInventory(),
            Component.literal("Account selection"),
            usedHand,
            new ArrayList<>(accounts.values()),
            currentAccount));
  }
}
