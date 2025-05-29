package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.network.PacketHandlerUtils;
import fr.kevyn.smp.ui.menu.IMenuActionHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MenuActionPacket(String id, String action, int amount)
    implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<MenuActionPacket> TYPE =
      new CustomPacketPayload.Type<>(
          ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "menu_action"));

  public static final StreamCodec<ByteBuf, MenuActionPacket> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      MenuActionPacket::id,
      ByteBufCodecs.STRING_UTF8,
      MenuActionPacket::action,
      ByteBufCodecs.VAR_INT,
      MenuActionPacket::amount,
      MenuActionPacket::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(final MenuActionPacket data, final IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      if (player.containerMenu instanceof IMenuActionHandler menu) {
        if (!menu.getMenuIdentifier().equals(data.id)) return;

        menu.handleMenuAction(data);
      }
    });
  }
}
