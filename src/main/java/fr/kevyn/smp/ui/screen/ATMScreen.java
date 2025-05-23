package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.ui.SilentButton;
import fr.kevyn.smp.ui.menu.ATMMenu;
import fr.kevyn.smp.utils.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ATMScreen extends AbstractScreen<ATMMenu> {
  private static final ResourceLocation TEXTURE =
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "textures/gui/atm/atm_gui.png");

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public ATMScreen(ATMMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
  }

  @Override
  protected void init() {
    super.init();

    int w = 40, h = 16;

    int r1 = this.topPos + 24, r2 = r1 + 24;
    int l = this.leftPos + 10, r = this.leftPos + this.imageWidth - w - 10;

    // top left
    this.addRenderableWidget(new SilentButton(l, r1, w, h, Component.literal("1€"), btn -> {
      menu.withdraw(1, hasShiftDown());
    }));

    // bottom left
    this.addRenderableWidget(new SilentButton(l, r2, w, h, Component.literal("10€"), btn -> {
      menu.withdraw(10, hasShiftDown());
    }));

    // top right
    this.addRenderableWidget(new SilentButton(r, r1, w, h, Component.literal("100€"), btn -> {
      menu.withdraw(100, hasShiftDown());
    }));

    // bottom right
    this.addRenderableWidget(new SilentButton(r, r2, w, h, Component.literal("1000€"), btn -> {
      menu.withdraw(1000, hasShiftDown());
    }));
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);

    if (this.menu.level.isClientSide()
        && this.minecraft instanceof Minecraft mc
        && mc.player instanceof LocalPlayer player) {
      var money = player.getData(SmpDataAttachments.MONEY);

      var moneyStr = NumberUtils.CURRENCY_FORMAT.format(money);

      // Calculate position to center the text
      int x = (width - imageWidth) / 2;
      int y = (height - imageHeight) / 2;
      // Render player name centered at the top of the ATM GUI
      guiGraphics.drawString(
          this.font,
          moneyStr,
          x + (imageWidth / 2) - (font.width(moneyStr) / 2), // Center horizontally
          y + 10, // Position it 10 pixels from the top of the GUI
          0x3F3F3F,
          false); // Dark gray color
    }
  }
}
