package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.ui.menu.IMenuNet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MenuActionNet(String id, String action, int amount) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<MenuActionNet> TYPE = new CustomPacketPayload.Type<>(
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "menu_action"));

  public static final StreamCodec<ByteBuf, MenuActionNet> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      MenuActionNet::id,
      ByteBufCodecs.STRING_UTF8,
      MenuActionNet::action,
      ByteBufCodecs.VAR_INT,
      MenuActionNet::amount,
      MenuActionNet::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(final MenuActionNet data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      if (context.player() instanceof ServerPlayer player
          && player.containerMenu instanceof IMenuNet menu) {
        if (!menu.getMenuIdentifier().equals(data.id)) return;

        menu.handleMenuAction(data);
      }
    });
  }
}
