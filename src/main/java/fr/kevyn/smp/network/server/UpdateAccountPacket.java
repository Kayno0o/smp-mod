package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.data.ServerAccountManager;
import fr.kevyn.smp.network.CustomByteBufCodecs;
import fr.kevyn.smp.network.PacketHandlerUtils;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.SoundUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateAccountPacket(UUID accountId, String name, Map<UUID, String> allowedAccess)
    implements CustomPacketPayload {
  public static final Type<UpdateAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "update_account"));

  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAccountPacket> STREAM_CODEC =
      StreamCodec.composite(
          CustomByteBufCodecs.UUID,
          UpdateAccountPacket::accountId,
          ByteBufCodecs.STRING_UTF8,
          UpdateAccountPacket::name,
          ByteBufCodecs.map(HashMap::new, CustomByteBufCodecs.UUID, ByteBufCodecs.STRING_UTF8),
          UpdateAccountPacket::allowedAccess,
          UpdateAccountPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(UpdateAccountPacket packet, IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      var accounts = ServerAccountManager.getAccounts(player.serverLevel());
      var oldAccount = accounts.get(packet.accountId());

      var playerId = player.getUUID();
      if (!oldAccount.owner().equals(playerId)) {
        SoundUtils.notify(player, SoundUtils.DISABLED);
        return;
      }

      var allowedAccessMap = new HashMap<>(packet.allowedAccess());

      while (allowedAccessMap.size() > AccountUtils.MAX_ALLOWED_ACCESS_COUNT) {
        var iterator = allowedAccessMap.keySet().iterator();
        iterator.next();
        iterator.remove();
      }

      if (!allowedAccessMap.containsKey(playerId))
        allowedAccessMap.put(playerId, player.getName().getString());

      var newAccount = new AccountEntry(
          oldAccount.id(), oldAccount.owner(), packet.name(), oldAccount.money(), allowedAccessMap);

      ServerAccountManager.putAccount(player.serverLevel(), oldAccount.id(), newAccount);
      SoundUtils.notify(player, SoundUtils.SUCCESS);

      Set<UUID> usersToNotify = new HashSet<>();
      usersToNotify.addAll(allowedAccessMap.keySet());
      usersToNotify.addAll(oldAccount.allowedAccess().keySet());

      for (var user : usersToNotify) AccountUtils.notifyPlayer(player.serverLevel(), user);
    });
  }
}
