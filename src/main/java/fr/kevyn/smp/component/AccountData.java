package fr.kevyn.smp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.kevyn.smp.network.CustomByteBufCodecs;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public record AccountData(UUID accountUUID) {
  public static final Codec<AccountData> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(CustomCodec.UUID.fieldOf("owner").forGetter(AccountData::accountUUID))
      .apply(instance, AccountData::new));

  public static final StreamCodec<ByteBuf, AccountData> STREAM_CODEC =
      StreamCodec.composite(CustomByteBufCodecs.UUID, AccountData::accountUUID, AccountData::new);

  public static AccountData create(@Nullable UUID ownerUUID) {
    return new AccountData(ownerUUID);
  }

  public static AccountData empty() {
    return new AccountData(null);
  }

  public boolean hasAccount() {
    return accountUUID != null;
  }
}
