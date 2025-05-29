package fr.kevyn.smp.network.client;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.network.CustomByteBufCodecs;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdatePlayerAccountsPacket(List<AccountEntry> accounts)
    implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UpdatePlayerAccountsPacket> TYPE =
      new CustomPacketPayload.Type<>(
          ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "local_accounts"));

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
    context
        .enqueueWork(() -> {
          if (context.player() instanceof LocalPlayer player) {
            Map<UUID, AccountEntry> accountMap = new HashMap<>();
            for (AccountEntry entry : data.accounts()) accountMap.put(entry.id(), entry);

            player.setData(SmpDataAttachments.ACCOUNTS, accountMap);
          }
        })
        .exceptionally(e -> {
          context.disconnect(Component.translatable("smp.networking.failed", e.getMessage()));
          return null;
        });
  }
}
