package fr.kevyn.smp.network;

import fr.kevyn.smp.init.DataAttachment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MoneyPayloadHandler {

  public static void handleDataOnNetwork(final MoneyData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      if (context.player() instanceof LocalPlayer player) {
        player.setData(DataAttachment.MONEY, data.money());
      }
    })
        .exceptionally(e -> {
          // Handle exception
          context.disconnect(Component.translatable("smp.networking.failed", e.getMessage()));
          return null;
        });
  }
}
