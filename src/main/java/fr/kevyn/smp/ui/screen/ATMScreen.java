package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.ui.SilentButton;
import fr.kevyn.smp.ui.menu.ATMMenu;
import fr.kevyn.smp.utils.AccountUtils;
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

  public static final int HEIGHT = 176;

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public ATMScreen(ATMMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = HEIGHT;
  }

  @Override
  protected void init() {
    super.init();

    int w = 40, h = 16;

    int r1 = this.topPos + 29;
    int r2 = r1 + h + 6;
    int l = getLeft();
    int r = getRight(w, 42);

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
      int money =
          AccountUtils.getMoney(this.menu.inventory.getStackInSlot(ATMMenu.CARD_SLOT), player);
      if (money == -1) return;

      var moneyStr = NumberUtils.CURRENCY_FORMAT.format(money);

      guiGraphics.drawString(
          this.font,
          moneyStr,
          getRight(font.width(moneyStr)),
          this.topPos + 6,
          SmpMod.LABEL_COLOR,
          false);
    }
  }
}
