package fr.kevyn.smp.ui.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractMenu<T extends AbstractContainerMenu, B extends BlockEntity>
    extends AbstractContainerMenu {
  public final B blockEntity;
  public final Level level;
  public final Player player;
  public final int slotsCount;

  protected AbstractMenu(
      MenuType<T> menuType, int containerId, B blockEntity, Player player, int slotsCount) {
    super(menuType, containerId);
    this.blockEntity = blockEntity;
    this.level = blockEntity.getLevel();
    this.player = player;
    this.slotsCount = slotsCount;
  }

  protected void addPlayerInventory(Inventory inv) {
    addPlayerInventory(inv, 166);
  }

  protected void addPlayerInventory(Inventory inv, int height) {
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, height - 82 + row * 18));
      }
    }
  }

  protected void addPlayerHotbar(Inventory inv) {
    addPlayerHotbar(inv, 166);
  }

  protected void addPlayerHotbar(Inventory inv, int height) {
    for (int col = 0; col < 9; ++col) {
      this.addSlot(new Slot(inv, col, 8 + col * 18, height - 24));
    }
  }

  // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
  private static final int HOTBAR_SLOT_COUNT = 9;
  private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
  private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
  private static final int PLAYER_INVENTORY_SLOT_COUNT =
      PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
  private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
  private static final int VANILLA_FIRST_SLOT_INDEX = 0;
  private static final int TE_INVENTORY_FIRST_SLOT_INDEX =
      VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

  @Override
  public ItemStack quickMoveStack(Player playerIn, int pIndex) {
    Slot sourceSlot = slots.get(pIndex);
    if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY; // EMPTY_ITEM
    ItemStack sourceStack = sourceSlot.getItem();
    ItemStack copyOfSourceStack = sourceStack.copy();

    // Check if the slot clicked is one of the vanilla container slots
    if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
      // This is a vanilla container slot so merge the stack into the tile inventory
      if (!moveItemStackTo(
          sourceStack,
          TE_INVENTORY_FIRST_SLOT_INDEX,
          TE_INVENTORY_FIRST_SLOT_INDEX + slotsCount,
          false)) {
        return ItemStack.EMPTY; // EMPTY_ITEM
      }
    } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + slotsCount) {
      // This is a TE slot so merge the stack into the players inventory
      if (!moveItemStackTo(
          sourceStack,
          VANILLA_FIRST_SLOT_INDEX,
          VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
          false)) {
        return ItemStack.EMPTY;
      }
    } else {
      System.out.println("Invalid slotIndex:" + pIndex);
      return ItemStack.EMPTY;
    }
    // If stack size == 0 (the entire stack was moved) set slot contents to null
    if (sourceStack.getCount() == 0) {
      sourceSlot.set(ItemStack.EMPTY);
    } else {
      sourceSlot.setChanged();
    }
    sourceSlot.onTake(playerIn, sourceStack);
    return copyOfSourceStack;
  }
}
