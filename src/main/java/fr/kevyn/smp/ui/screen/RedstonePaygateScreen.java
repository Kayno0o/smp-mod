package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.block.RedstonePaygateBlockEntity;
import fr.kevyn.smp.network.server.MenuActionNet;
import fr.kevyn.smp.ui.menu.RedstonePaygateMenu;
import fr.kevyn.smp.ui.widget.SilentButton;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class RedstonePaygateScreen extends AbstractMenuScreen<RedstonePaygateMenu> {
  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
      SmpMod.MODID, "textures/gui/redstone_paygate/redstone_paygate_gui.png");

  public static final int HEIGHT = 162;

  private EditBox amountInput;

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public RedstonePaygateScreen(RedstonePaygateMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = HEIGHT;
  }

  @Override
  protected void init() {
    super.init();

    int inputWidth = 54;
    int h = 16;
    int inputX = getLeft();
    int inputY = topPos + 39;

    amountInput = new EditBox(font, inputX, inputY, inputWidth, h, Component.literal("Amount"));
    amountInput.setFilter(s -> s.matches("\\d*"));
    amountInput.setMaxLength(3);
    amountInput.setValue(String.valueOf(menu.blockEntity.getPrice()));
    amountInput.setResponder((String value) -> {
      int price = value.length() > 0 ? Integer.parseInt(value) : 0;

      menu.blockEntity.setPrice(price);
      PacketDistributor.sendToServer(
          new MenuActionNet(menu.getMenuIdentifier(), RedstonePaygateMenu.ACTION_SET_PRICE, price));
    });
    this.addRenderableWidget(amountInput);

    int withdrawWidth = 56;
    int withdrawX = getRight(withdrawWidth);
    int withdrawY = topPos + 39;
    this.addRenderableWidget(new SilentButton(
        withdrawX, withdrawY, withdrawWidth, h, Component.literal("Withdraw"), btn -> {
          PacketDistributor.sendToServer(
              new MenuActionNet(menu.getMenuIdentifier(), RedstonePaygateMenu.ACTION_WITHDRAW, -1));
        }));
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);

    guiGraphics.drawString(font, "Price", 8, 27, SmpMod.LABEL_COLOR, false);
    // guiGraphics.drawString(font, "Balance:", 114, 27, SmpMod.LABEL_COLOR, false);

  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);

    var balanceStr = String.valueOf(menu.blockEntity.getBalance()) + "/"
        + String.valueOf(RedstonePaygateBlockEntity.MAX_BALANCE) + " €";
    guiGraphics.drawString(
        font,
        balanceStr,
        getRight(font.width(balanceStr)),
        this.topPos + 27,
        SmpMod.LABEL_COLOR,
        false);

    if (this.menu.level.isClientSide()
        && this.minecraft instanceof Minecraft mc
        && mc.player instanceof LocalPlayer player) {
      int money = AccountUtils.getMoney(
          this.menu.blockEntity.inventory.getStackInSlot(RedstonePaygateBlockEntity.CARD_SLOT),
          player);
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
