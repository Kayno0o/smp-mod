package fr.kevyn.smp.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.kevyn.smp.projectile.CoinProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class CoinProjectileRenderer extends ThrownItemRenderer<CoinProjectile> {
  private final ItemRenderer itemRenderer;

  public CoinProjectileRenderer(EntityRendererProvider.Context context) {
    super(context, 1, true);
    this.itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(
      CoinProjectile entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    if (entity.tickCount >= 4
        || this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) >= 12.25) {
      poseStack.pushPose();

      poseStack.scale(0.2f, 0.2f, 0.2f);

      poseStack.translate(0.0D, 0.15D, 0.0D);

      // Rotate around axis to simulate spinning
      float rotation = (entity.tickCount + partialTicks) * 20.0f;
      poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
      poseStack.mulPose(Axis.XP.rotationDegrees(90)); // tilt forward
      poseStack.mulPose(Axis.ZP.rotationDegrees(0));

      this.itemRenderer.renderStatic(
          entity.getItem(),
          ItemDisplayContext.GROUND,
          packedLight,
          OverlayTexture.NO_OVERLAY,
          poseStack,
          buffer,
          entity.level(),
          entity.getId());

      poseStack.popPose();
      super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
  }
}
