package fr.kevyn.artisanspath.network.client;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.data.AccountEntry;
import fr.kevyn.artisanspath.init.ArtisansDataAttachments;
import fr.kevyn.artisanspath.network.CustomByteBufCodecs;
import fr.kevyn.artisanspath.network.PacketHandlerUtils;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdatePlayerAccountsPacket(List<AccountEntry> accounts)
    implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UpdatePlayerAccountsPacket> TYPE =
      new CustomPacketPayload.Type<>(
          ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "local_accounts"));

  public static final StreamCodec<ByteBuf, AccountEntry> ENTRY_CODEC = StreamCodec.composite(
      CustomByteBufCodecs.UUID,
      AccountEntry::id,
      CustomByteBufCodecs.UUID,
      AccountEntry::owner,
      ByteBufCodecs.STRING_UTF8,
      AccountEntry::name,
      ByteBufCodecs.VAR_INT,
      AccountEntry::money,
      ByteBufCodecs.map(HashMap::new, CustomByteBufCodecs.UUID, ByteBufCodecs.STRING_UTF8),
      AccountEntry::allowedAccess,
      AccountEntry::new);

  public static final StreamCodec<ByteBuf, UpdatePlayerAccountsPacket> STREAM_CODEC =
      ByteBufCodecs.<ByteBuf, AccountEntry>list()
          .apply(ENTRY_CODEC)
          .map(UpdatePlayerAccountsPacket::new, UpdatePlayerAccountsPacket::accounts);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnClient(
      final UpdatePlayerAccountsPacket data, final IPayloadContext context) {
    PacketHandlerUtils.enqueueClientWork(context, player -> {
      Map<UUID, AccountEntry> accountMap = new HashMap<>();
      for (AccountEntry entry : data.accounts()) accountMap.put(entry.id(), entry);

      player.setData(ArtisansDataAttachments.ACCOUNTS, accountMap);
    });
  }
}
