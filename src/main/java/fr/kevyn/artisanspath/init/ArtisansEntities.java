package fr.kevyn.artisanspath.init;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.projectile.CoinProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArtisansEntities {
  private ArtisansEntities() {}

  public static final DeferredRegister<EntityType<?>> REGISTRY =
      DeferredRegister.create(Registries.ENTITY_TYPE, ArtisansMod.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<CoinProjectile>> COIN_PROJECTILE =
      REGISTRY.register("coin_projectile", () -> EntityType.Builder.<CoinProjectile>of(
              CoinProjectile::new, MobCategory.MISC)
          .sized(0.25F, 0.25F)
          .clientTrackingRange(4)
          .updateInterval(10)
          .build(ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "coin_projectile")
              .toString()));
}
