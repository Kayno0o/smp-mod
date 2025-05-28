package fr.kevyn.smp.network.server;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.utils.AccountUtils;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
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

public record SetCardAccountPacket(InteractionHand hand, UUID accountId)
    implements CustomPacketPayload {
  public static final Type<SetCardAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "set_card_account"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SetCardAccountPacket> STREAM_CODEC =
      StreamCodec.composite(
          NeoForgeStreamCodecs.enumCodec(InteractionHand.class),
          SetCardAccountPacket::hand,
          UUIDUtil.STREAM_CODEC,
          SetCardAccountPacket::accountId,
          SetCardAccountPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handleOnServer(SetCardAccountPacket packet, IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      ItemStack stack = player.getItemInHand(packet.hand());

      if (stack.getItem() instanceof CardItem
          && AccountUtils.hasAccessToAccount(packet.accountId(), player, player.serverLevel())) {
        AccountUtils.setAccount(stack, packet.accountId(), player.serverLevel());

        // Play sound
        player
            .level()
            .playSound(
                player,
                player.getOnPos(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.BLOCKS,
                1f,
                1f);
      }
    });
  }
}
