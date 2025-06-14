package fr.kevyn.artisanspath.network.server;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.data.ServerAccountManager;
import fr.kevyn.artisanspath.network.CustomByteBufCodecs;
import fr.kevyn.artisanspath.network.PacketHandlerUtils;
import fr.kevyn.artisanspath.utils.AccountUtils;
import fr.kevyn.artisanspath.utils.SoundUtils;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeaveAccountPacket(UUID accountId) implements CustomPacketPayload {
  public static final Type<LeaveAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "leave_account"));

  public static final StreamCodec<RegistryFriendlyByteBuf, LeaveAccountPacket> STREAM_CODEC =
      StreamCodec.composite(
          CustomByteBufCodecs.UUID, LeaveAccountPacket::accountId, LeaveAccountPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(LeaveAccountPacket packet, IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      var level = player.serverLevel();
      UUID playerId = player.getUUID();

      var account = ServerAccountManager.getAccount(level, packet.accountId());
      if (account.owner().equals(playerId)) {
        SoundUtils.notify(player, SoundUtils.DISABLED);
        return;
      }

      var removed = ServerAccountManager.leaveAccount(level, packet.accountId(), playerId);
      if (removed) {
        SoundUtils.notify(player, SoundUtils.DELETE);
        AccountUtils.notifyPlayer(level, playerId);
      }
    });
  }
}
