package fr.kevyn.smp.network;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.codec.StreamCodec;

public final class CustomByteBufCodecs {
  public static final StreamCodec<ByteBuf, UUID> UUID = new StreamCodec<ByteBuf, UUID>() {
    @Override
    public UUID decode(ByteBuf buf) {
      long mostSigBits = buf.readLong();
      long leastSigBits = buf.readLong();
      return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public void encode(ByteBuf buf, UUID uuid) {
      buf.writeLong(uuid.getMostSignificantBits());
      buf.writeLong(uuid.getLeastSignificantBits());
    }
  };
}
