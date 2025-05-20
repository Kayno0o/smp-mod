package fr.kevyn.smp.network;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.DataAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MoneyData(int money) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<MoneyData> TYPE = new CustomPacketPayload.Type<>(
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "money"));

  public static final StreamCodec<ByteBuf, MoneyData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      MoneyData::money,
      MoneyData::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnClient(final MoneyData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      if (context.player() instanceof LocalPlayer player) {
        player.setData(DataAttachment.MONEY, data.money());
      }
    })
        .exceptionally(e -> {
          // Handle exception
          context.disconnect(Component.translatable("smp.networking.failed", e.getMessage()));
          return null;
        });
  }
}
