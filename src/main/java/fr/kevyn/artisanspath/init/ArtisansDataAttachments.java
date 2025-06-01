package fr.kevyn.artisanspath.init;

import com.mojang.serialization.Codec;
import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.data.AccountEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.UUIDUtil;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ArtisansDataAttachments {
  private ArtisansDataAttachments() {}

  public static final DeferredRegister<AttachmentType<?>> REGISTRY =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ArtisansMod.MODID);

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<UUID, AccountEntry>>>
      ACCOUNTS =
          REGISTRY.register("accounts", () -> AttachmentType.<Map<UUID, AccountEntry>>builder(
                  (Supplier<Map<UUID, AccountEntry>>) HashMap::new)
              .serialize(Codec.unboundedMap(UUIDUtil.CODEC, AccountEntry.CODEC))
              .copyOnDeath()
              .copyHandler((original, holder, provider) -> new HashMap<>(original))
              .build());
}
