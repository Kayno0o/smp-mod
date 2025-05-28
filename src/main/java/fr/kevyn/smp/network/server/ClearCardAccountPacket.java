package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.utils.AccountUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      ItemStack stack = player.getItemInHand(packet.hand());

      if (stack.getItem() instanceof CardItem) {
        AccountUtils.clearAccount(stack);

        // Play sound
        player
            .level()
            .playSound(
                player, player.getOnPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1f, 0f);
      }
    });
  }
}
