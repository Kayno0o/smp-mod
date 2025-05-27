package fr.kevyn.smp.init;

import com.mojang.serialization.Codec;
import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.component.LocalAccountEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.UUIDUtil;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SmpDataAttachments {
  public static final DeferredRegister<AttachmentType<?>> REGISTRY =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SmpMod.MODID);

  public static final DeferredHolder<
          AttachmentType<?>, AttachmentType<Map<UUID, LocalAccountEntry>>>
      LOCAL_ACCOUNTS =
          REGISTRY.register("local_accounts", () -> (AttachmentType<Map<UUID, LocalAccountEntry>>)
              AttachmentType.<Map<UUID, LocalAccountEntry>>builder(
                      (Supplier<Map<UUID, LocalAccountEntry>>) HashMap::new)
                  .serialize(Codec.unboundedMap(UUIDUtil.CODEC, LocalAccountEntry.CODEC))
                  .copyOnDeath()
                  .copyHandler((original, holder, provider) -> new HashMap<>(original))
                  .build());
}
