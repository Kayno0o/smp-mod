package fr.kevyn.smp.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MultiSearchablePlayerSelect extends EditBox {
  private Map<UUID, String> allPlayers = new HashMap<>();
  private List<Map.Entry<UUID, String>> filteredPlayers = new ArrayList<>();
  private Map<UUID, String> selectedPlayers = new HashMap<>();
  private boolean dropdownOpen = false;
  private int selectedIndex = -1;
  private final OnSelectionChange onSelectionChange;
  private boolean isSearching = false;

  private static final int MAX_DROPDOWN_HEIGHT = 120;
  private static final int ITEM_HEIGHT = 14;

  public MultiSearchablePlayerSelect(
      Font font,
      int x,
      int y,
      int width,
      int height,
      OnSelectionChange onSelectionChange,
      Map<UUID, String> allPlayers,
      Map<UUID, String> preSelected) {
    super(font, x, y, width, height, CommonComponents.EMPTY);
    this.allPlayers = new HashMap<>(allPlayers);
    this.onSelectionChange = onSelectionChange;

    if (preSelected != null) {
      this.selectedPlayers.putAll(preSelected);
    }

    // Set responder to detect value changes
    setResponder(value -> {
      if (isSearching) {
        filterPlayers();
      }
    });

    updateDisplayText();
    filterPlayers();
  }

  public MultiSearchablePlayerSelect(
      Font font, int x, int y, int width, int height, OnSelectionChange onSelectionChange) {
    this(
        font,
        x,
        y,
        width,
        height,
        onSelectionChange,
        Minecraft.getInstance().level.players().stream()
            .collect(Collectors.toMap(Player::getUUID, p -> p.getName().getString())),
        null);
  }

  private void filterPlayers() {
    filteredPlayers.clear();
    String search = getValue().toLowerCase().trim();

    if (search.isEmpty()) {
      // Show all players when not searching
      filteredPlayers.addAll(allPlayers.entrySet());
    } else {
      // Filter by search term
      for (var entry : allPlayers.entrySet()) {
        if (entry.getValue().toLowerCase().contains(search)) {
          filteredPlayers.add(entry);
        }
      }
    }

    // Sort: selected players first, then alphabetical
    filteredPlayers.sort((a, b) -> {
      boolean aSelected = selectedPlayers.containsKey(a.getKey());
      boolean bSelected = selectedPlayers.containsKey(b.getKey());

      if (aSelected && !bSelected) return -1;
      if (!aSelected && bSelected) return 1;
      return a.getValue().compareToIgnoreCase(b.getValue());
    });

    selectedIndex = -1;
  }

  private void updateDisplayText() {
    if (!isSearching) {
      if (selectedPlayers.isEmpty()) {
        setValue("Click to select players...");
      } else {
        setValue(selectedPlayers.size() + " player" + (selectedPlayers.size() == 1 ? "" : "s")
            + " selected");
      }
    }
  }

  private void startSearch() {
    isSearching = true;
    setValue("");
    filterPlayers();
  }

  private void stopSearch() {
    isSearching = false;
    updateDisplayText();
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (!isSearching) {
      startSearch();
    }

    boolean result = super.charTyped(codePoint, modifiers);
    filterPlayers();
    dropdownOpen = true;
    return result;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (button == 0) {
      if (dropdownOpen && isInDropdownArea(mouseX, mouseY)) {
        int clickedIndex = (int) ((mouseY - (getY() + height)) / ITEM_HEIGHT);
        if (clickedIndex >= 0 && clickedIndex < filteredPlayers.size()) {
          togglePlayer(filteredPlayers.get(clickedIndex));
        }
        return true;
      } else if (isInBounds(mouseX, mouseY)) {
        setFocused(true);
        if (!isSearching) {
          startSearch();
        }
        return super.mouseClicked(mouseX, mouseY, button);
      } else {
        setFocused(false);
      }
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  private boolean isInDropdownArea(double mouseX, double mouseY) {
    int dropdownHeight = Math.min(filteredPlayers.size() * ITEM_HEIGHT, MAX_DROPDOWN_HEIGHT);
    return mouseX >= getX()
        && mouseX <= getX() + width
        && mouseY >= getY() + height
        && mouseY <= getY() + height + dropdownHeight;
  }

  private boolean isInBounds(double mouseX, double mouseY) {
    return mouseX >= getX()
        && mouseX <= getX() + width
        && mouseY >= getY()
        && mouseY <= getY() + height;
  }

  private void togglePlayer(Map.Entry<UUID, String> entry) {
    UUID id = entry.getKey();
    if (selectedPlayers.containsKey(id)) {
      selectedPlayers.remove(id);
    } else {
      selectedPlayers.put(id, entry.getValue());
    }

    filterPlayers(); // Refresh to update sorting
    if (onSelectionChange != null) {
      onSelectionChange.onSelectionChange(this);
    }
  }

  public Map<UUID, String> getSelectedPlayers() {
    return new HashMap<>(selectedPlayers);
  }

  public void setSelectedPlayers(Map<UUID, String> selected) {
    selectedPlayers.clear();
    if (selected != null) {
      selectedPlayers.putAll(selected);
    }
    updateDisplayText();
    filterPlayers();
  }

  public void addPlayer(UUID id, String name) {
    allPlayers.put(id, name);
    filterPlayers();
  }

  public void clearSelection() {
    selectedPlayers.clear();
    updateDisplayText();
    filterPlayers();
    if (onSelectionChange != null) {
      onSelectionChange.onSelectionChange(this);
    }
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
    int visibleItems = Math.min(filteredPlayers.size(), MAX_DROPDOWN_HEIGHT / ITEM_HEIGHT);
    int dropdownHeight = visibleItems * ITEM_HEIGHT;
    int dropdownWidth = width; // Match input width exactly

    // Background with border
    guiGraphics.fill(
        getX(), dropdownY, getX() + dropdownWidth, dropdownY + dropdownHeight, 0xFF000000);
    guiGraphics.fill(
        getX() + 1,
        dropdownY + 1,
        getX() + dropdownWidth - 1,
        dropdownY + dropdownHeight - 1,
        0xFF404040);

    Font font = Minecraft.getInstance().font;

    for (int i = 0; i < visibleItems; i++) {
      var entry = filteredPlayers.get(i);
      int itemY = dropdownY + i * ITEM_HEIGHT;
      boolean isSelected = selectedPlayers.containsKey(entry.getKey());
      boolean isHovered = i == selectedIndex;

      // Background colors
      if (isHovered) {
        guiGraphics.fill(
            getX() + 1, itemY + 1, getX() + dropdownWidth - 1, itemY + ITEM_HEIGHT - 1, 0xFF606060);
      }
      if (isSelected) {
        guiGraphics.fill(
            getX() + 1,
            itemY + 1,
            getX() + dropdownWidth - 1,
            itemY + ITEM_HEIGHT - 1,
            isHovered ? 0xFF507050 : 0xFF405040);
      }

      // Text with icons - ensure it doesn't overflow
      String icon = isSelected ? "[x] " : "[ ] ";
      String text = icon + entry.getValue();

      // Trim text if it would overflow
      int maxTextWidth = dropdownWidth - 8; // Account for padding
      if (font.width(text) > maxTextWidth) {
        while (font.width(text + "...") > maxTextWidth && text.length() > icon.length()) {
          text = text.substring(0, text.length() - 1);
        }
        text += "...";
      }

      int textColor = isSelected ? 0xAAFFAA : 0xFFFFFF;
      guiGraphics.drawString(font, text, getX() + 4, itemY + 3, textColor);
    }

    // Scroll indicator if needed
    if (filteredPlayers.size() > visibleItems) {
      String scrollText = "... " + (filteredPlayers.size() - visibleItems) + " more";
      guiGraphics.drawString(
          font, scrollText, getX() + 4, dropdownY + dropdownHeight - 12, 0x888888);
    }
  }

  @Override
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    if (!focused) {
      stopSearch();
      dropdownOpen = false;
    } else {
      startSearch();
      dropdownOpen = true;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public interface OnSelectionChange {
    void onSelectionChange(MultiSearchablePlayerSelect select);
  }
}
