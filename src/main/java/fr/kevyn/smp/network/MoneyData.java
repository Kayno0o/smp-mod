package fr.kevyn.smp.network;

import fr.kevyn.smp.SmpMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

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
}
