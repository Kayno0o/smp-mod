package fr.kevyn.smp.atm;

import com.mojang.blaze3d.systems.RenderSystem;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.custom.SilentButton;
import fr.kevyn.smp.helper.MoneyHelper;
import fr.kevyn.smp.init.DataAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ATMScreen extends AbstractContainerScreen<ATMMenu> {
  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SmpMod.MODID,
      "textures/gui/atm/atm_gui.png");

  public ATMScreen(ATMMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageWidth = 176;
    this.imageHeight = 166;
  }

  @Override
  protected void init() {
    super.init();

    int w = 40, h = 16;

    int r1 = this.topPos + 24, r2 = r1 + 24;
    int l = this.leftPos + 10, r = this.leftPos + this.imageWidth - w - 10;

    // top left
    this.addRenderableWidget(new SilentButton(Component.literal("1€"), l, r1, w, h, btn -> {
      menu.withdraw(1, hasShiftDown());
    }));

    // bottom left
    this.addRenderableWidget(new SilentButton(Component.literal("10€"), l, r2, w, h, btn -> {
      menu.withdraw(10, hasShiftDown());
    }));

    // top right
    this.addRenderableWidget(new SilentButton(Component.literal("100€"), r, r1, w, h, btn -> {
      menu.withdraw(100, hasShiftDown());
    }));

    // bottom right
    this.addRenderableWidget(new SilentButton(Component.literal("1000€"), r, r2, w, h, btn -> {
      menu.withdraw(1000, hasShiftDown());
    }));
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);

    int x = (width - imageWidth) / 2;
    int y = (height - imageHeight) / 2;

    guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
  }

  @Override
  public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
    super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

    if (this.menu.level.isClientSide() && this.minecraft instanceof Minecraft mc
        && mc.player instanceof LocalPlayer player) {
      var money = player.getData(DataAttachment.MONEY);

      var moneyStr = MoneyHelper.CURRENCY_FORMAT.format(money);

      // Calculate position to center the text
      int x = (width - imageWidth) / 2;
      int y = (height - imageHeight) / 2;
      // Render player name centered at the top of the ATM GUI
      pGuiGraphics.drawString(this.font, moneyStr,
          x + (imageWidth / 2) - (font.width(moneyStr) / 2), // Center horizontally
          y + 10, // Position it 10 pixels from the top of the GUI
          0x3F3F3F, false); // Dark gray color
    }
  }
}