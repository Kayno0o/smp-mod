package fr.kevyn.smp.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.ui.menu.AbstractMenu;
import fr.kevyn.smp.utils.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractScreen<T extends AbstractMenu<?, ?>>
    extends AbstractContainerScreen<T> {
  protected abstract ResourceLocation getTexture();

  public AbstractScreen(T menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageWidth = 176;
    this.imageHeight = 166;
  }

  @Override
  protected void init() {
    super.init();

    this.inventoryLabelX = 8;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, getTexture());

    int x = (width - imageWidth) / 2;
    int y = (height - imageHeight) / 2;

    guiGraphics.blit(getTexture(), x, y, 0, 0, this.imageWidth, this.imageHeight);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(
        this.font, this.title, this.titleLabelX, this.titleLabelY, SmpMod.LABEL_COLOR, false);
    guiGraphics.drawString(
        this.font,
        this.playerInventoryTitle,
        this.inventoryLabelX,
        this.inventoryLabelY,
        SmpMod.LABEL_COLOR,
        false);
  }

  protected int getCenterX() {
    return GuiUtils.getCenterX(this.leftPos, this.imageWidth);
  }

  protected int getCenterX(int width) {
    return GuiUtils.getCenterX(this.leftPos, this.imageWidth, width);
  }

  protected int getCenterY() {
    return GuiUtils.getCenterY(this.topPos, this.imageHeight);
  }

  protected int getCenterY(int height) {
    return GuiUtils.getCenterY(this.topPos, this.imageHeight, height);
  }

  protected int getLeft() {
    return this.leftPos + 8;
  }

  protected int getLeft(int padding) {
    return this.leftPos + padding;
  }

  protected int getRight(int width) {
    return this.leftPos + this.imageWidth - width - 8;
  }

  protected int getRight(int width, int padding) {
    return this.leftPos + this.imageWidth - width - padding;
  }

  protected int getRightLabel(int width) {
    return this.imageWidth - width - 8;
  }
}
