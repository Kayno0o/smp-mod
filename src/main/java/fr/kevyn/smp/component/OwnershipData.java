package fr.kevyn.smp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record OwnershipData(String ownerUUID) {
  public static final String EMPTY = "";

  public static final Codec<OwnershipData> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(Codec.STRING.fieldOf("owner").forGetter(OwnershipData::ownerUUID))
      .apply(instance, OwnershipData::new));

  public static final StreamCodec<ByteBuf, OwnershipData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, OwnershipData::ownerUUID, OwnershipData::new);

  public static OwnershipData create(String ownerUUID) {
    return new OwnershipData(ownerUUID != null ? ownerUUID : EMPTY);
  }

  public static OwnershipData empty() {
    return new OwnershipData(EMPTY);
  }

  public boolean hasOwner() {
    return ownerUUID != null && !ownerUUID.isEmpty();
  }
}
