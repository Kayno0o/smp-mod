package fr.kevyn.smp.renderer;

import fr.kevyn.smp.projectile.CoinProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class CoinProjectileRenderer extends ThrownItemRenderer<CoinProjectile> {
  public CoinProjectileRenderer(EntityRendererProvider.Context context) {
    super(context, 1.0F, true);
  }

  // @Override
  // public ResourceLocation getTextureLocation(Entity entity) {
  //   return ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "textures/item/coin.png");
  // }

}
