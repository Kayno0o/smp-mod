package fr.kevyn.smp.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.kevyn.smp.block.RedstonePaygateBlockEntity;
import fr.kevyn.smp.utils.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RedstonePaygateRenderer implements BlockEntityRenderer<RedstonePaygateBlockEntity> {
  public RedstonePaygateRenderer(BlockEntityRendererProvider.Context context) {}

  @Override
  public void render(
      RedstonePaygateBlockEntity blockEntity,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    if (Minecraft.renderNames()) {
      Minecraft mc = Minecraft.getInstance();

      if (mc.player == null
          || mc.hitResult == null
          || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

      BlockPos hitPos = ((BlockHitResult) mc.hitResult).getBlockPos();
      if (!hitPos.equals(blockEntity.getBlockPos())) return;

      double distSq = mc.player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos()));
      if (distSq > 64) return;

      String text = "Price: " + NumberUtils.CURRENCY_FORMAT.format(blockEntity.getPrice());
      Font font = mc.font;

      poseStack.pushPose();

      Vec3 cameraPos = mc.getEntityRenderDispatcher().camera.getPosition();
      Vec3 blockCenter = Vec3.atCenterOf(blockEntity.getBlockPos());

      Vec3 direction = cameraPos.subtract(blockCenter).normalize();

      double offsetDistance = 1;
      poseStack.translate(
          0.5 + direction.x * offsetDistance,
          0.5 + direction.y * offsetDistance,
          0.5 + direction.z * offsetDistance);

      poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
      poseStack.scale(0.01f, -0.01f, 0.01f);

      font.drawInBatch(
          text,
          -font.width(text) / 2f,
          0,
          0xFFFFFFFF,
          false,
          poseStack.last().pose(),
          buffer,
          Font.DisplayMode.NORMAL,
          0,
          packedLight);

      poseStack.popPose();
    }
  }
}
