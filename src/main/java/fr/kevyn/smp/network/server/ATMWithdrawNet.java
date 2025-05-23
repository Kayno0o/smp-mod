package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.init.SmpItems;
import fr.kevyn.smp.network.client.UpdateMoneyNet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ATMWithdrawNet(int money, int count) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<ATMWithdrawNet> TYPE =
      new CustomPacketPayload.Type<>(
          ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "money_withdraw"));

  public static final StreamCodec<ByteBuf, ATMWithdrawNet> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      ATMWithdrawNet::money,
      ByteBufCodecs.VAR_INT,
      ATMWithdrawNet::count,
      ATMWithdrawNet::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(final ATMWithdrawNet data, final IPayloadContext context) {
    context
        .enqueueWork(() -> {
          if (context.player() instanceof ServerPlayer player) {
            var moneyWithdraw = data.money();
            var countWithdraw = data.count();

            var moneyStack = SmpItems.moneyStackFromValue(moneyWithdraw, countWithdraw);

            int total = moneyWithdraw * countWithdraw;

            moneyStack.ifPresent(stack -> {
              int current = player.getData(SmpDataAttachments.MONEY);
              if (current < total) {
                player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);

                return;
              }

              player.setData(SmpDataAttachments.MONEY, current - total);
              player.getInventory().add(stack);
              player.playNotifySound(
                  SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.PLAYERS, 1.0f, 1.0f);

              if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new UpdateMoneyNet(current - total));
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
