package fr.kevyn.smp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;

public record LocalAccountEntry(UUID id, String name, int money) {

  public static final Codec<LocalAccountEntry> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              CustomCodec.UUID.fieldOf("uuid").forGetter(LocalAccountEntry::id),
              Codec.STRING.fieldOf("name").forGetter(LocalAccountEntry::name),
              Codec.INT.fieldOf("money").forGetter(LocalAccountEntry::money))
          .apply(instance, LocalAccountEntry::new));
}
