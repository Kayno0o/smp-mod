package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.ServerAccountManager;
import fr.kevyn.smp.network.CustomByteBufCodecs;
import fr.kevyn.smp.network.PacketHandlerUtils;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.SoundUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DeleteAccountPacket(UUID accountId) implements CustomPacketPayload {
  public static final Type<DeleteAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "delete_account"));

  public static final StreamCodec<RegistryFriendlyByteBuf, DeleteAccountPacket> STREAM_CODEC =
      StreamCodec.composite(
          CustomByteBufCodecs.UUID, DeleteAccountPacket::accountId, DeleteAccountPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(DeleteAccountPacket packet, IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      var level = player.serverLevel();
      var account = ServerAccountManager.getAccount(level, packet.accountId());
      if (!account.owner().equals(player.getUUID())) {
        SoundUtils.notify(player, SoundUtils.DISABLED);
        return;
      }

      var money = account.money();
      var totalPlayers = account.allowedAccess().size();
      var moneyPerPlayer = money / totalPlayers;
      var remainder = money % totalPlayers;

      List<Map.Entry<UUID, Integer>> moneyDistribution = new ArrayList<>();
      int index = 0;
      for (var playerId : account.allowedAccess().keySet()) {
        int playerMoney = moneyPerPlayer + (index < remainder ? 1 : 0);
        moneyDistribution.add(Map.entry(playerId, playerMoney));
        index++;
      }

      for (var entry : moneyDistribution)
        AccountUtils.addMoney(entry.getKey(), level, entry.getValue());

      SoundUtils.notify(player, SoundUtils.SUCCESS);

      ServerAccountManager.removeAccount(level, account.id());

      var players = account.allowedAccess();
      for (var playerId : players.keySet()) AccountUtils.notifyPlayer(level, playerId);
    });
  }
}
