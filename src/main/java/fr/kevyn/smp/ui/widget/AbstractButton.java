package fr.kevyn.smp.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.kevyn.smp.SmpMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractButton extends Button {
  protected static final WidgetSprites SPRITES = new WidgetSprites(
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "widget/button"),
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "widget/button_disabled"),
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "widget/button_highlighted"));

  protected AbstractButton(
      int x, int y, int width, int height, Component message, OnPress onPress) {
    super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
  }

  @Override
  protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    Minecraft minecraft = Minecraft.getInstance();
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
    RenderSystem.enableBlend();
    RenderSystem.enableDepthTest();
    guiGraphics.blitSprite(
        SPRITES.get(this.active, this.isHoveredOrFocused()),
        this.getX(),
        this.getY(),
        this.getWidth(),
        this.getHeight());
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    int i = getFGColor();
    this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
  }
}
