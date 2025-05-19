package fr.kevyn.smp.network;

import fr.kevyn.smp.init.DataAttachment;
import fr.kevyn.smp.init.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ATMWithdrawHandler {

  public static void handleDataOnNetwork(final ATMWithdraw data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      if (context.player() instanceof ServerPlayer player) {
        var toWithdraw = data.money();
        var moneyStack = Items.moneyStackFromValue(toWithdraw, 1);
        moneyStack.ifPresent(stack -> {
          int current = player.getData(DataAttachment.MONEY);
          player.setData(DataAttachment.MONEY, current - toWithdraw);
          player.getInventory().add(stack);

          if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new MoneyData(current - toWithdraw));
          }
        });
      }
    })
        .exceptionally(e -> {
          // Handle exception
          context.disconnect(Component.translatable("smp.networking.failed", e.getMessage()));
          return null;
        });
  }
}
