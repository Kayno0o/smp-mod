package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.data.ServerAccountManager;
import fr.kevyn.smp.network.PacketHandlerUtils;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.SoundUtils;
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
      new Type<>(ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "create_account"));

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
