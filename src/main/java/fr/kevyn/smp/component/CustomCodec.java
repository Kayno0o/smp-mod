package fr.kevyn.smp.component;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import java.util.UUID;

public class CustomCodec {
  public static final PrimitiveCodec<UUID> UUID = new PrimitiveCodec<UUID>() {
    @Override
    public <T> DataResult<UUID> read(DynamicOps<T> ops, T input) {
      return ops.getStringValue(input).flatMap(str -> {
        try {
          return DataResult.success(java.util.UUID.fromString(str));
        } catch (IllegalArgumentException e) {
          return DataResult.error(() -> "Invalid UUID string: " + str);
        }
      });
    }

    @Override
    public <T> T write(final DynamicOps<T> ops, final UUID value) {
      return ops.createString(value.toString());
    }

    @Override
    public String toString() {
      return "UUID";
    }
  };
}
