package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.network.server.DeleteAccountPacket;
import fr.kevyn.smp.network.server.UpdateAccountPacket;
import fr.kevyn.smp.ui.menu.BaseMenu;
import fr.kevyn.smp.ui.widget.MultiSearchablePlayerSelect;
import fr.kevyn.smp.ui.widget.SilentButton;
import fr.kevyn.smp.ui.widget.SilentImageButton;
import fr.kevyn.smp.utils.NumberUtils;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class EditAccountScreen extends AbstractMenuScreen<BaseMenu> {
  private String name = "";
  private final AccountEntry account;
  private Map<UUID, String> allowedAccess;

  public static final int HEIGHT = 194;

  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
      SmpMod.MODID, "textures/gui/account_selection/account_selection_gui.png");

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public EditAccountScreen(BaseMenu menu, Inventory playerInventory, AccountEntry account) {
    super(menu, playerInventory, Component.translatable("gui.smp.edit_account.title"));
    this.imageHeight = HEIGHT;
    this.account = account;
    this.name = account.name();
    this.allowedAccess = account.allowedAccess();
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
    var nameInput = new EditBox(
        font, getCenterX(160), getTop(), 160, 16, Component.translatable("gui.smp.name"));
    nameInput.setMaxLength(16);
    nameInput.setValue(this.account.name());
    nameInput.setResponder(newName -> this.name = newName);
    this.addRenderableWidget(nameInput);

    var level = Minecraft.getInstance().level;
    Map<UUID, String> playerMap = level.players().stream()
        .filter(p -> !p.getUUID().equals(account.owner()))
        .collect(Collectors.toMap(Player::getUUID, p -> p.getName().getString()));

    // Add allowed access players (includes offline players)
    playerMap.putAll(account.allowedAccess());

    var playerSelect = new MultiSearchablePlayerSelect(
        font,
        getCenterX(160),
        getTop() + 16 + PADDING_X,
        160,
        16,
        select -> this.allowedAccess = select.getSelectedPlayers(),
        playerMap,
        this.account.allowedAccess());
    this.addRenderableWidget(playerSelect);

    int btnH = 16;

    // confirm button
    var confirmButton = new SilentButton(
        getLeft(),
        getBottom(btnH),
        60,
        btnH,
        Component.translatable("gui.smp.confirm").withStyle(ChatFormatting.GREEN),
        btn -> this.updateAccount());

    this.addRenderableWidget(confirmButton);

    // delete button
    SilentImageButton deleteButton = new SilentImageButton(
        getRight(16) - 48 - PADDING_X,
        getBottom(btnH),
        16,
        btnH,
        DELETE_BUTTON,
        btn -> this.deleteAccount());

    this.addRenderableWidget(deleteButton);

    // cancel button
    SilentButton cancelButton = new SilentButton(
        getRight(48),
        getBottom(btnH),
        48,
        btnH,
        Component.translatable("gui.smp.cancel").withStyle(ChatFormatting.GRAY),
        btn -> this.goBack());

    this.addRenderableWidget(cancelButton);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);

    var moneyStr = NumberUtils.getCurrencyFormat().format(this.account.money());
    guiGraphics.drawString(
        this.font,
        moneyStr,
        getRight(font.width(moneyStr)),
        this.topPos + 6,
        SmpMod.LABEL_COLOR,
        false);
  }

  private void goBack() {
    Minecraft.getInstance().setScreen(new AccountsManagerScreen(this.menu, this.playerInventory));
  }

  private void updateAccount() {
    PacketDistributor.sendToServer(
        new UpdateAccountPacket(this.account.id(), this.name, this.allowedAccess));
    this.goBack();
  }

  private void deleteAccount() {
    PacketDistributor.sendToServer(new DeleteAccountPacket(this.account.id()));
    this.goBack();
  }
}
