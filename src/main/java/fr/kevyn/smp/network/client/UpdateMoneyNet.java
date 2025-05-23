package fr.kevyn.smp.network.client;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.SmpDataAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateMoneyNet(int money) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UpdateMoneyNet> TYPE =
      new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "money"));

  public static final StreamCodec<ByteBuf, UpdateMoneyNet> STREAM_CODEC =
      StreamCodec.composite(ByteBufCodecs.VAR_INT, UpdateMoneyNet::money, UpdateMoneyNet::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnClient(final UpdateMoneyNet data, final IPayloadContext context) {
    context
        .enqueueWork(() -> {
          if (context.player() instanceof LocalPlayer player) {
            player.setData(SmpDataAttachments.MONEY, data.money());
          }
        })
        .exceptionally(e -> {
          // Handle exception
          context.disconnect(Component.translatable("smp.networking.failed", e.getMessage()));
          return null;
        });
  }
}
