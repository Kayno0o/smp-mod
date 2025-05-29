package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.SmpComponents;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.network.PacketHandlerUtils;
import fr.kevyn.smp.utils.SoundUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClearCardAccountPacket(InteractionHand hand) implements CustomPacketPayload {
  public static final Type<ClearCardAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "clear_card_account"));

  public static final StreamCodec<RegistryFriendlyByteBuf, ClearCardAccountPacket> STREAM_CODEC =
      StreamCodec.composite(
          NeoForgeStreamCodecs.enumCodec(InteractionHand.class),
          ClearCardAccountPacket::hand,
          ClearCardAccountPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(ClearCardAccountPacket packet, IPayloadContext context) {
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      ItemStack stack = player.getItemInHand(packet.hand());

      if (stack.getItem() instanceof CardItem) {
        stack.remove(SmpComponents.OWNERSHIP);

        SoundUtils.notify(player, SoundUtils.DELETE);
      }
    });
  }
}
