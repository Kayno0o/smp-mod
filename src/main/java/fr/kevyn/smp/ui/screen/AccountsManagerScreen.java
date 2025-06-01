package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.network.server.LeaveAccountPacket;
import fr.kevyn.smp.ui.menu.BaseMenu;
import fr.kevyn.smp.ui.widget.SilentButton;
import fr.kevyn.smp.ui.widget.SilentImageButton;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.NumberUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class AccountsManagerScreen extends AbstractMenuScreen<BaseMenu> {
  private List<AccountEntry> accounts = new ArrayList<>();

  public static final int HEIGHT = 194;

  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
      SmpMod.MODID, "textures/gui/account_selection/account_selection_gui.png");

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public AccountsManagerScreen(BaseMenu menu, Inventory playerInventory) {
    super(menu, playerInventory, Component.translatable("gui.smp.accounts_management.title"));
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
    this.accounts =
        new ArrayList<>(this.player.getData(SmpDataAttachments.ACCOUNTS).values());

    int buttonWidth = 160;
    int buttonHeight = 16;
    int paddingY = 6;
    int paddingX = 8;
    int startY = getTop();
    int startX = getLeft();
    int actionButtonSize = 16;

    var playerId = Minecraft.getInstance().player.getUUID();

    // account buttons
    accounts.sort((a, b) -> a.name().compareTo(b.name()));
    for (int i = 0; i < accounts.size(); i++) {
      AccountEntry account = accounts.get(i);
      int y = startY + i * (buttonHeight + paddingY);

      boolean canEdit = account.owner().equals(playerId) && !account.id().equals(playerId);
      boolean canLeave = !account.owner().equals(playerId) && !account.id().equals(playerId);

      ChatFormatting buttonColor = ChatFormatting.WHITE;

      SilentButton accountButton = new SilentButton(
          startX,
          y,
          canEdit || canLeave ? buttonWidth - actionButtonSize - paddingX : buttonWidth,
          buttonHeight,
          Component.literal(account.name())
              .append(Component.literal(" - "))
              .append(Component.literal(NumberUtils.getCurrencyFormat().format(account.money())))
              .append(canEdit ? " *" : "")
              .withStyle(buttonColor),
          btn -> {});
      accountButton.active = false;

      this.addRenderableWidget(accountButton);

      if (canEdit) {
        SilentImageButton actionButton = new SilentImageButton(
            getRight(actionButtonSize),
            y,
            actionButtonSize,
            actionButtonSize,
            EDIT_BUTTON,
            btn -> editAccount(account));

        this.addRenderableWidget(actionButton);
      } else if (canLeave) {
        SilentImageButton actionButton = new SilentImageButton(
            getRight(actionButtonSize),
            y,
            actionButtonSize,
            actionButtonSize,
            DELETE_BUTTON,
            btn -> leaveAccount(account.id()));

        this.addRenderableWidget(actionButton);
      }
    }

    // cancel button
    SilentButton cancelButton = new SilentButton(
        getRight(48),
        getBottom(buttonHeight),
        48,
        buttonHeight,
        Component.translatable("gui.smp.cancel").withStyle(ChatFormatting.GRAY),
        btn -> this.onClose());

    this.addRenderableWidget(cancelButton);

    // create button
    if (accounts.size() < AccountUtils.MAX_ACCOUNT) {
      SilentButton createAccountButton = new SilentButton(
          getLeft(),
          getBottom(buttonHeight),
          100,
          buttonHeight,
          Component.translatable("gui.smp.create_account").withStyle(ChatFormatting.GREEN),
          btn -> this.createAccount());

      this.addRenderableWidget(createAccountButton);
    }
  }

  private void editAccount(AccountEntry account) {
    Minecraft.getInstance()
        .setScreen(new EditAccountScreen(this.menu, this.playerInventory, account));
  }

  private void leaveAccount(UUID accountId) {
    PacketDistributor.sendToServer(new LeaveAccountPacket(accountId));
  }

  private void createAccount() {
    Minecraft.getInstance().setScreen(new CreateAccountScreen(this.menu, this.playerInventory));
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);

    if (AccountUtils.accountsChanged(
        this.player.getData(SmpDataAttachments.ACCOUNTS), this.accounts)) this.rebuildWidgets();
  }
}
