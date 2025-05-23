package fr.kevyn.smp.init;

import com.mojang.serialization.Codec;
import fr.kevyn.smp.SmpMod;
import java.util.function.Supplier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SmpDataAttachments {
  public static final DeferredRegister<AttachmentType<?>> REGISTRY =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SmpMod.MODID);

  public static final Supplier<AttachmentType<Integer>> MONEY = REGISTRY.register(
      "money",
      () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
}
