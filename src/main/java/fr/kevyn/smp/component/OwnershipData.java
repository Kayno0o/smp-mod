package fr.kevyn.smp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Component to store ownership data for items
 */
public record OwnershipData(String ownerUUID) {
  public static final String EMPTY = "";

  public static final Codec<OwnershipData> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(Codec.STRING.fieldOf("owner").forGetter(OwnershipData::ownerUUID))
      .apply(instance, OwnershipData::new));

  public static final StreamCodec<ByteBuf, OwnershipData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, OwnershipData::ownerUUID,
      OwnershipData::new);

  /**
   * Creates a new OwnershipData with the specified owner
   * 
   * @param ownerUUID The name of the owner
   * @return A new OwnershipData instance
   */
  public static OwnershipData create(String ownerUUID) {
    return new OwnershipData(ownerUUID != null ? ownerUUID : EMPTY);
  }

  /**
   * Creates an empty OwnershipData with no owner
   * 
   * @return An empty OwnershipData instance
   */
  public static OwnershipData empty() {
    return new OwnershipData(EMPTY);
  }

  /**
   * Checks if this ownership data has a valid owner
   * 
   * @return true if has owner, false otherwise
   */
  public boolean hasOwner() {
    return ownerUUID != null && !ownerUUID.isEmpty();
  }
}
