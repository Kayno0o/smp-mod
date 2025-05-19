package fr.kevyn.smp.network;

import fr.kevyn.smp.SmpMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ATMWithdraw(int money, int count) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<ATMWithdraw> TYPE = new CustomPacketPayload.Type<>(
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "money_withdraw"));

  public static final StreamCodec<ByteBuf, ATMWithdraw> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      ATMWithdraw::money,
      ByteBufCodecs.VAR_INT,
      ATMWithdraw::count,
      ATMWithdraw::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
