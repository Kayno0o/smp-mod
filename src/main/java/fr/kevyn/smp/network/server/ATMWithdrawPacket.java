package fr.kevyn.smp.network.server;

import fr.kevyn.smp.init.SmpItems;
import fr.kevyn.smp.network.CustomByteBufCodecs;
import fr.kevyn.smp.network.PacketFactory;
import fr.kevyn.smp.network.PacketHandlerUtils;
import fr.kevyn.smp.utils.AccountUtils;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ATMWithdrawPacket(UUID account, int money, int count) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<ATMWithdrawPacket> TYPE =
      PacketFactory.createType("money_withdraw");

  public static final StreamCodec<ByteBuf, ATMWithdrawPacket> STREAM_CODEC = StreamCodec.composite(
      CustomByteBufCodecs.UUID,
      ATMWithdrawPacket::account,
      ByteBufCodecs.VAR_INT,
      ATMWithdrawPacket::money,
      ByteBufCodecs.VAR_INT,
      ATMWithdrawPacket::count,
      ATMWithdrawPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(ATMWithdrawPacket packet, IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      var moneyWithdraw = packet.money();
      var countWithdraw = packet.count();

      var moneyStack = SmpItems.moneyStackFromValue(moneyWithdraw, countWithdraw);

      int total = moneyWithdraw * countWithdraw;

      moneyStack.ifPresent(stack -> {
        if (!AccountUtils.hasAccessToAccount(packet.account(), player, player.level())) return;
        if (!AccountUtils.addMoney(packet.account(), (ServerLevel) player.level(), -total)) {
          player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);

          return;
        }

        player.getInventory().add(stack);
        player.playNotifySound(
            SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
      });
    });
  }
}
