package fr.kevyn.artisanspath.network.server;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.item.PaymentCardItem;
import fr.kevyn.artisanspath.network.PacketHandlerUtils;
import fr.kevyn.artisanspath.utils.AccountUtils;
import fr.kevyn.artisanspath.utils.SoundUtils;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetCardAccountPacket(InteractionHand hand, UUID accountId)
    implements CustomPacketPayload {
  public static final Type<SetCardAccountPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "set_card_account"));

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
    PacketHandlerUtils.enqueueServerWork(context, player -> {
      ItemStack stack = player.getItemInHand(packet.hand());

      if (stack.getItem() instanceof PaymentCardItem
          && AccountUtils.hasAccessToAccount(packet.accountId(), player, player.serverLevel())) {
        AccountUtils.setAccount(stack, packet.accountId(), player.serverLevel());

        SoundUtils.notify(player, SoundUtils.SUCCESS);
      }
    });
  }
}
