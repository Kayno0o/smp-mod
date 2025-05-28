package fr.kevyn.smp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;

public record LocalAccountEntry(UUID id, UUID owner, String name, int money) {

  public static final Codec<LocalAccountEntry> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              CustomCodec.UUID.fieldOf("id").forGetter(LocalAccountEntry::id),
              CustomCodec.UUID.fieldOf("owner").forGetter(LocalAccountEntry::owner),
              Codec.STRING.fieldOf("name").forGetter(LocalAccountEntry::name),
              Codec.INT.fieldOf("money").forGetter(LocalAccountEntry::money))
          .apply(instance, LocalAccountEntry::new));
}
