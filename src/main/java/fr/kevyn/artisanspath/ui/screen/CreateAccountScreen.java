package fr.kevyn.artisanspath.ui.screen;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.network.server.CreateAccountPacket;
import fr.kevyn.artisanspath.ui.menu.BaseMenu;
import fr.kevyn.artisanspath.ui.widget.SilentButton;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class CreateAccountScreen extends AbstractMenuScreen<BaseMenu> {
  private EditBox nameInput;

  public static final int HEIGHT = 194;

  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
      ArtisansMod.MODID, "textures/gui/account_selection/account_selection_gui.png");

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public CreateAccountScreen(BaseMenu menu, Inventory playerInventory) {
    super(menu, playerInventory, Component.translatable("gui.artisanspath.create_account.title"));
    this.imageHeight = HEIGHT;
  }

  @Override
  protected boolean showInventory() {
    return false;
  }

  @Override
  protected void init() {
    super.init();
    this.addWidgets();
  }

  private void addWidgets() {
    nameInput = new EditBox(
        font, getCenterX(160), getTop(), 160, 16, Component.translatable("gui.artisanspath.name"));
    nameInput.setMaxLength(16);
    this.addRenderableWidget(nameInput);

    int btnH = 16;

    // confirm button
    var confirmButton = new SilentButton(
        getLeft(),
        getBottom(btnH),
        60,
        btnH,
        Component.translatable("gui.artisanspath.confirm").withStyle(ChatFormatting.GREEN),
        btn -> this.createAccount());

    this.addRenderableWidget(confirmButton);

    // cancel button
    SilentButton cancelButton = new SilentButton(
        getRight(40),
        getBottom(btnH),
        40,
        btnH,
        Component.translatable("gui.artisanspath.cancel").withStyle(ChatFormatting.GRAY),
        btn -> this.goBack());

    this.addRenderableWidget(cancelButton);
  }

  private void goBack() {
    Minecraft.getInstance().setScreen(new AccountsManagerScreen(this.menu, this.playerInventory));
  }

  private void createAccount() {
    var name = nameInput.getValue();
    PacketDistributor.sendToServer(new CreateAccountPacket(name));
    this.goBack();
  }
}
