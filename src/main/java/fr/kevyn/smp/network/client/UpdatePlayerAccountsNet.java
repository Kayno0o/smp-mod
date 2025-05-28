package fr.kevyn.smp.network.client;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.component.LocalAccountEntry;
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

public record UpdatePlayerAccountsNet(List<LocalAccountEntry> accounts)
    implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UpdatePlayerAccountsNet> TYPE =
      new CustomPacketPayload.Type<>(
          ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "local_accounts"));

  public static final StreamCodec<ByteBuf, LocalAccountEntry> ACCOUNT_ENTRY_CODEC =
      StreamCodec.composite(
          CustomByteBufCodecs.UUID,
          LocalAccountEntry::id,
          CustomByteBufCodecs.UUID,
          LocalAccountEntry::owner,
          ByteBufCodecs.STRING_UTF8,
          LocalAccountEntry::name,
          ByteBufCodecs.VAR_INT,
          LocalAccountEntry::money,
          LocalAccountEntry::new);

  public static final StreamCodec<ByteBuf, UpdatePlayerAccountsNet> STREAM_CODEC =
      ByteBufCodecs.<ByteBuf, LocalAccountEntry>list()
          .apply(ACCOUNT_ENTRY_CODEC)
          .map(UpdatePlayerAccountsNet::new, UpdatePlayerAccountsNet::accounts);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnClient(
      final UpdatePlayerAccountsNet data, final IPayloadContext context) {
    context
        .enqueueWork(() -> {
          if (context.player() instanceof LocalPlayer player) {
            Map<UUID, LocalAccountEntry> accountMap = new HashMap<>();
            for (LocalAccountEntry entry : data.accounts()) accountMap.put(entry.id(), entry);

            player.setData(SmpDataAttachments.LOCAL_ACCOUNTS, accountMap);
          }
        })
        .exceptionally(e -> {
          context.disconnect(Component.translatable("smp.networking.failed", e.getMessage()));
          return null;
        });
  }
}
