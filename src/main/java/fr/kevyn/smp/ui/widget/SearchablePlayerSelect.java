package fr.kevyn.smp.ui.widget;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class SearchablePlayerSelect extends EditBox {
  private List<AbstractClientPlayer> allPlayers = new ArrayList<>();
  private List<AbstractClientPlayer> filteredPlayers = new ArrayList<>();
  private boolean dropdownOpen = false;
  private int selectedIndex = -1;
  private final OnSelectPlayer onSelectPlayer;

  @Nullable private AbstractClientPlayer selectedPlayer = null;

  private static final int MAX_DROPDOWN_HEIGHT = 100;

  public SearchablePlayerSelect(
      Font font,
      int x,
      int y,
      int width,
      int height,
      OnSelectPlayer onSelectPlayer,
      List<AbstractClientPlayer> allPlayers) {
    super(font, x, y, width, height, CommonComponents.EMPTY);
    this.allPlayers = allPlayers;
    this.onSelectPlayer = onSelectPlayer;
    filterPlayers();
  }

  public SearchablePlayerSelect(
      Font font, int x, int y, int width, int height, OnSelectPlayer onSelectPlayer) {
    this(
        font, x, y, width, height, onSelectPlayer, Minecraft.getInstance().level.players());
  }

  private void filterPlayers() {
    filteredPlayers.clear();
    String search = this.getValue().toLowerCase();

    for (AbstractClientPlayer player : allPlayers) {
      if (player.getName().getString().toLowerCase().contains(search)) {
        filteredPlayers.add(player);
      }
    }
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    boolean result = super.charTyped(codePoint, modifiers);
    filterPlayers();
    this.dropdownOpen = !filteredPlayers.isEmpty();
    selectedIndex = -1;
    return result;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (dropdownOpen) {
      if (keyCode == GLFW.GLFW_KEY_DOWN) {
        selectedIndex = Math.min(selectedIndex + 1, filteredPlayers.size() - 1);
        return true;
      }

      if (keyCode == GLFW.GLFW_KEY_UP) {
        selectedIndex = Math.max(selectedIndex - 1, -1);
        return true;
      }

      if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_TAB) {
        if (selectedIndex >= 0 && selectedIndex < filteredPlayers.size()) {
          selectPlayer(filteredPlayers.get(selectedIndex));
        }
        return true;
      }

      if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
        dropdownOpen = false;
        return true;
      }
    }

    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (button == 0) {
      // Check if clicking in dropdown area
      if (dropdownOpen && isInDropdownArea(mouseX, mouseY)) {
        int itemHeight = 12;
        int clickedIndex = (int) ((mouseY - (getY() + height)) / itemHeight);
        if (clickedIndex >= 0 && clickedIndex < filteredPlayers.size()) {
          selectPlayer(filteredPlayers.get(clickedIndex));
        }
        return true;
      } else if (!isInBounds(mouseX, mouseY)) {
        dropdownOpen = false;
      }
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  private boolean isInDropdownArea(double mouseX, double mouseY) {
    return mouseX >= getX()
        && mouseX <= getX() + width
        && mouseY >= getY() + height
        && mouseY <= getY() + height + Math.min(filteredPlayers.size() * 12, MAX_DROPDOWN_HEIGHT);
  }

  private boolean isInBounds(double mouseX, double mouseY) {
    return mouseX >= getX()
        && mouseX <= getX() + width
        && mouseY >= getY()
        && mouseY <= getY() + height;
  }

  private void selectPlayer(AbstractClientPlayer player) {
    selectedPlayer = player;
    setValue(player.getName().getString());
    dropdownOpen = false;
    selectedIndex = -1;
    this.onSelectPlayer.onSelectPlayer(this);
  }

  public AbstractClientPlayer getSelectedPlayer() {
    return selectedPlayer;
  }

  public void setSelectedPlayer(@Nullable AbstractClientPlayer player) {
    this.selectedPlayer = player;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

    if (dropdownOpen && !filteredPlayers.isEmpty()) {
      renderDropdown(guiGraphics, mouseX, mouseY);
    }
  }

  private void renderDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    int dropdownY = getY() + height;
    int itemHeight = 12;
    int dropdownHeight = Math.min(filteredPlayers.size() * itemHeight, MAX_DROPDOWN_HEIGHT);

    // Background
    guiGraphics.fill(getX(), dropdownY, getX() + width, dropdownY + dropdownHeight, 0xFF000000);
    guiGraphics.fill(
        getX() + 1, dropdownY + 1, getX() + width - 1, dropdownY + dropdownHeight - 1, 0xFF404040);

    // Items
    for (int i = 0; i < filteredPlayers.size() && i * itemHeight < MAX_DROPDOWN_HEIGHT; i++) {
      int itemY = dropdownY + i * itemHeight;

      // Highlight selected item
      if (i == selectedIndex) {
        guiGraphics.fill(getX() + 1, itemY, getX() + width - 1, itemY + itemHeight, 0xFF606060);
      }

      // Render player name
      guiGraphics.drawString(
          Minecraft.getInstance().font,
          filteredPlayers.get(i).getName().getString(),
          getX() + 4,
          itemY + 2,
          0xFFFFFF);
    }
  }

  @Override
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    if (focused) {
      dropdownOpen = !filteredPlayers.isEmpty();
    } else {
      dropdownOpen = false;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public interface OnSelectPlayer {
    void onSelectPlayer(SearchablePlayerSelect select);
  }
}
