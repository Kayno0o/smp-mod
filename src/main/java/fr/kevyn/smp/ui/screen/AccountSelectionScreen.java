package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.network.server.ClearCardAccountPacket;
import fr.kevyn.smp.network.server.SetCardAccountPacket;
import fr.kevyn.smp.ui.menu.AccountSelectionMenu;
import fr.kevyn.smp.ui.widget.SilentButton;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.NumberUtils;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class AccountSelectionScreen extends AbstractMenuScreen<AccountSelectionMenu> {
  private final InteractionHand hand;
  private final List<AccountEntry> accounts;
  private final boolean hasExistingAccount;

  @Nullable private final UUID currentAccount;

  public static final int HEIGHT = 194;

  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
      SmpMod.MODID, "textures/gui/account_selection/account_selection_gui.png");

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public AccountSelectionScreen(
      AccountSelectionMenu menu,
      Inventory playerInventory,
      InteractionHand hand,
      List<AccountEntry> accounts,
      @Nullable UUID currentAccount) {
    super(menu, playerInventory, Component.translatable("gui.smp.account_selection.title"));
    this.hand = hand;
    this.accounts = accounts;
    this.hasExistingAccount = currentAccount != null;
    this.imageHeight = HEIGHT;
    this.currentAccount = currentAccount;
  }

  @Override
  protected boolean showInventory() {
    return false;
  }

  @Override
  protected void init() {
    super.init();

    int buttonWidth = 160;
    int buttonHeight = 16;
    int paddingY = 6;
    int startY = getTop();
    int centerX = this.getCenterX(buttonWidth);

    // account buttons
    accounts.sort((a, b) -> a.name().compareTo(b.name()));
    for (int i = 0; i < accounts.size(); i++) {
      AccountEntry account = accounts.get(i);
      int y = startY + i * (buttonHeight + paddingY);

      SilentButton accountButton = new SilentButton(
          centerX,
          y,
          buttonWidth,
          buttonHeight,
          Component.literal(account.name())
              .append(Component.literal(" - "))
              .append(Component.literal(NumberUtils.getCurrencyFormat().format(account.money())))
              .withStyle(
                  account.id().equals(this.currentAccount)
                      ? ChatFormatting.YELLOW
                      : ChatFormatting.WHITE),
          btn -> selectAccount(account.id()));

      this.addRenderableWidget(accountButton);
    }

    // clear button if there's an existing account
    if (hasExistingAccount) {
      SilentButton clearButton = new SilentButton(
          getLeft(),
          getBottom(buttonHeight),
          64,
          buttonHeight,
          Component.translatable("gui.smp.clear").withStyle(ChatFormatting.RED),
          btn -> clearAccount());

      this.addRenderableWidget(clearButton);
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
  }

  private void selectAccount(UUID accountId) {
    PacketDistributor.sendToServer(new SetCardAccountPacket(hand, accountId));
    this.onClose();
  }

  private void clearAccount() {
    PacketDistributor.sendToServer(new ClearCardAccountPacket(hand));
    this.onClose();
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);

    if (AccountUtils.accountsChanged(
        this.player.getData(SmpDataAttachments.ACCOUNTS), this.accounts)) this.rebuildWidgets();
  }
}
