package fr.kevyn.artisanspath.network.server;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.data.AccountEntry;
import fr.kevyn.artisanspath.data.ServerAccountManager;
import fr.kevyn.artisanspath.network.PacketHandlerUtils;
import fr.kevyn.artisanspath.utils.AccountUtils;
import fr.kevyn.artisanspath.utils.SoundUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CreateAccountPacket(String name) implements CustomPacketPayload {
  public static final Type<CreateAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "create_account"));

  public static final StreamCodec<RegistryFriendlyByteBuf, CreateAccountPacket> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.STRING_UTF8, CreateAccountPacket::name, CreateAccountPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(CreateAccountPacket packet, IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      var accounts = ServerAccountManager.getAccounts(player.serverLevel());
      var playerAccounts = AccountUtils.getPlayerAccounts(accounts, player.getUUID());
      if (playerAccounts.size() >= AccountUtils.MAX_ACCOUNT) {
        SoundUtils.notify(player, SoundUtils.DISABLED);
        return;
      }

      var playerId = player.getUUID();
      var accountId = UUID.randomUUID();

      Map<UUID, String> allowedAccess = new HashMap<>();
      allowedAccess.put(playerId, player.getName().getString());

      AccountEntry account = new AccountEntry(accountId, playerId, packet.name(), 0, allowedAccess);
      ServerAccountManager.putAccount(player.serverLevel(), accountId, account);

      SoundUtils.notify(player, SoundUtils.SUCCESS);
      AccountUtils.notifyPlayer(player.serverLevel(), playerId);
    });
  }
}
