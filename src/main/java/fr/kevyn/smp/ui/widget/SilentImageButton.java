package fr.kevyn.smp.ui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class SilentImageButton extends SilentButton {
  protected final WidgetSprites sprites;

  public SilentImageButton(
      int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress) {
    super(x, y, width, height, CommonComponents.EMPTY, onPress);
    this.sprites = sprites;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    ResourceLocation resourcelocation =
        this.sprites.get(this.isActive(), this.isHoveredOrFocused());
    guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
  }
}
