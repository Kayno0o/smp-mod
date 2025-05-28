package fr.kevyn.smp.ui.screen;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.component.LocalAccountEntry;
import fr.kevyn.smp.ui.menu.AccountsManagerMenu;
import fr.kevyn.smp.ui.widget.SilentButton;
import fr.kevyn.smp.ui.widget.SilentImageButton;
import fr.kevyn.smp.utils.NumberUtils;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AccountsManagerScreen extends AbstractMenuScreen<AccountsManagerMenu> {
  private final List<LocalAccountEntry> accounts;

  public static final int HEIGHT = 194;

  private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
      SmpMod.MODID, "textures/gui/account_selection/account_selection_gui.png");

  private static final WidgetSprites EDIT_ICON = new WidgetSprites(
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "icons/edit"),
      ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, "icons/edit_highlighted"));

  public ResourceLocation getTexture() {
    return TEXTURE;
  }

  public AccountsManagerScreen(
      AccountsManagerMenu menu,
      Inventory playerInventory,
      Component title,
      List<LocalAccountEntry> accounts) {
    super(menu, playerInventory, title);
    this.accounts = accounts;
    this.imageHeight = HEIGHT;
  }

  @Override
  protected boolean hasInventory() {
    return false;
  }

  @Override
  protected void init() {
    super.init();

    int buttonWidth = 160;
    int buttonHeight = 16;
    int paddingY = 6;
    int paddingX = 8;
    int startY = getTop();
    int startX = getLeft();
    int editButtonSize = 16;

    var playerId = Minecraft.getInstance().player.getUUID();

    // account buttons
    for (int i = 0; i < accounts.size(); i++) {
      LocalAccountEntry account = accounts.get(i);
      int y = startY + i * (buttonHeight + paddingY);

      boolean canEdit = account.owner().equals(playerId) && !account.id().equals(playerId);
      SilentButton accountButton = new SilentButton(
          startX,
          y,
          buttonWidth - editButtonSize - paddingX,
          buttonHeight,
          Component.literal(account.name())
              .append(Component.literal(" - "))
              .append(Component.literal(NumberUtils.CURRENCY_FORMAT.format(account.money())))
              .append(canEdit ? " *" : "")
              .withStyle(canEdit ? ChatFormatting.GREEN : ChatFormatting.BLUE),
          btn -> {});
      accountButton.active = false;

      this.addRenderableWidget(accountButton);

      if (canEdit) {
        SilentImageButton editButton = new SilentImageButton(
            getRight(editButtonSize),
            y,
            editButtonSize,
            editButtonSize,
            EDIT_ICON,
            btn -> editAccount(account.id())); // Your edit method

        this.addRenderableWidget(editButton);
      }
    }

    // cancel button
    SilentButton cancelButton = new SilentButton(
        getRight(40),
        getBottom(buttonHeight),
        40,
        buttonHeight,
        Component.literal("Cancel").withStyle(ChatFormatting.GRAY),
        btn -> this.onClose());

    this.addRenderableWidget(cancelButton);
  }

  private void editAccount(UUID accountId) {}

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }
}
