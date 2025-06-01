package fr.kevyn.artisanspath.item.handler;

import fr.kevyn.artisanspath.init.ArtisansDataAttachments;
import fr.kevyn.artisanspath.ui.menu.AccountSelectionMenu;
import fr.kevyn.artisanspath.ui.screen.AccountSelectionScreen;
import fr.kevyn.artisanspath.utils.AccountUtils;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientCardHandler {
  private ClientCardHandler() {}

  public static void openSelectionScreen(LocalPlayer localPlayer, InteractionHand usedHand) {
    var accounts = localPlayer.getData(ArtisansDataAttachments.ACCOUNTS);

    UUID currentAccount = AccountUtils.getAccountUUID(localPlayer.getItemInHand(usedHand));

    AccountSelectionMenu menu = new AccountSelectionMenu(0, localPlayer.getInventory());

    Minecraft.getInstance()
        .setScreen(new AccountSelectionScreen(
            menu,
            localPlayer.getInventory(),
            usedHand,
            new ArrayList<>(accounts.values()),
            currentAccount));
  }
}
