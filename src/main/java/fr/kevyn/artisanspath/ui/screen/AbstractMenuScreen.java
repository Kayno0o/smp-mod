package fr.kevyn.artisanspath.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.ui.menu.AbstractMenu;
import fr.kevyn.artisanspath.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractMenuScreen<T extends AbstractMenu<?>>
    extends AbstractContainerScreen<T> {
  protected abstract ResourceLocation getTexture();

  protected final LocalPlayer player;
  protected final Inventory playerInventory;

  public static final WidgetSprites EDIT_BUTTON = new WidgetSprites(
      ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "icons/edit"),
      ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "icons/edit_highlighted"));

  public static final WidgetSprites DELETE_BUTTON = new WidgetSprites(
      ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "icons/delete"),
      ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "icons/delete_highlighted"));

  public static final int INVENTORY_HEIGHT = 101;
  public static final int PADDING_X = 8;

  protected boolean showInventory() {
    return true;
  }

  protected AbstractMenuScreen(T menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageWidth = 176;
    this.imageHeight = 166;
    this.playerInventory = inv;
    this.player = Minecraft.getInstance().player;
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
        this.font, this.title, this.titleLabelX, this.titleLabelY, ArtisansMod.LABEL_COLOR, false);

    if (showInventory())
      guiGraphics.drawString(
          this.font,
          this.playerInventoryTitle,
          this.inventoryLabelX,
          this.inventoryLabelY,
          ArtisansMod.LABEL_COLOR,
          false);
  }

  protected int getCenterX() {
    return GuiUtils.getCenterX(this.leftPos, this.imageWidth);
  }

  protected int getCenterX(int width) {
    return GuiUtils.getCenterX(this.leftPos, this.imageWidth, width);
  }

  protected int getCenterY() {
    return GuiUtils.getCenterY(
        this.topPos, this.imageHeight - (showInventory() ? INVENTORY_HEIGHT : 0));
  }

  protected int getCenterY(int height) {
    return GuiUtils.getCenterY(
        this.topPos, this.imageHeight - (showInventory() ? INVENTORY_HEIGHT : 0), height);
  }

  protected int getTop() {
    return getTop(8);
  }

  protected int getTop(int padding) {
    return this.topPos + 21 + padding;
  }

  protected int getHeaderTop() {
    return this.getHeaderTop(8);
  }

  protected int getHeaderTop(int padding) {
    return this.topPos + padding;
  }

  protected int getRight(int width) {
    return getRight(width, 8);
  }

  protected int getRight(int width, int padding) {
    return this.leftPos + this.imageWidth - width - padding;
  }

  protected int getBottom(int height) {
    return getBottom(height, 8);
  }

  protected int getBottom(int height, int padding) {
    return this.topPos
        + this.imageHeight
        - (showInventory() ? INVENTORY_HEIGHT : 0)
        - padding
        - height;
  }

  protected int getLeft() {
    return getLeft(8);
  }

  protected int getLeft(int padding) {
    return this.leftPos + padding;
  }

  protected int getRightLabel(int width) {
    return this.imageWidth - width - 8;
  }
}
