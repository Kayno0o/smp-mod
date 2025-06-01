package fr.kevyn.artisanspath.ui.screen;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.ui.menu.ATMMenu;
import fr.kevyn.artisanspath.ui.widget.SilentButton;
import fr.kevyn.artisanspath.utils.AccountUtils;
import fr.kevyn.artisanspath.utils.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ATMScreen extends AbstractMenuScreen<ATMMenu> {
  private static final ResourceLocation TEXTURE =
      ResourceLocation.fromNamespaceAndPath(ArtisansMod.MODID, "textures/gui/atm/atm_gui.png");

  public static final int HEIGHT = 176;

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public ATMScreen(ATMMenu menu, Inventory inv, Component title) {
    super(menu, inv, Component.translatable("gui.artisanspath.atm.title"));
    this.imageHeight = HEIGHT;
  }

  @Override
  protected void init() {
    super.init();

    int w = 48;
    int h = 16;

    int r1 = this.topPos + 29;
    int r2 = r1 + h + 6;
    int l = getLeft();
    int r = getRight(w, 42);

    // top left
    this.addRenderableWidget(new SilentButton(
        l,
        r1,
        w,
        h,
        Component.literal(NumberUtils.getCurrencyFormat().format(1)),
        btn -> menu.withdraw(1, hasShiftDown())));

    // bottom left
    this.addRenderableWidget(new SilentButton(
        l,
        r2,
        w,
        h,
        Component.literal(NumberUtils.getCurrencyFormat().format(10)),
        btn -> menu.withdraw(10, hasShiftDown())));

    // top right
    this.addRenderableWidget(new SilentButton(
        r,
        r1,
        w,
        h,
        Component.literal(NumberUtils.getCurrencyFormat().format(100)),
        btn -> menu.withdraw(100, hasShiftDown())));

    // bottom right
    this.addRenderableWidget(new SilentButton(
        r,
        r2,
        w,
        h,
        Component.literal(NumberUtils.getCurrencyFormat().format(1000)),
        btn -> menu.withdraw(1000, hasShiftDown())));
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

      var moneyStr = NumberUtils.getCurrencyFormat().format(money);

      guiGraphics.drawString(
          this.font,
          moneyStr,
          getRight(font.width(moneyStr)),
          this.topPos + 6,
          ArtisansMod.LABEL_COLOR,
          false);
    }
  }
}
