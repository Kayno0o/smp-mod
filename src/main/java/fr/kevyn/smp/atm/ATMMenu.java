package fr.kevyn.smp.atm;

import fr.kevyn.smp.init.Blocks;
import fr.kevyn.smp.init.DataAttachment;
import fr.kevyn.smp.init.Menus;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.item.MoneyItem;
import fr.kevyn.smp.network.ATMWithdraw;
import fr.kevyn.smp.network.MoneyData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class ATMMenu extends AbstractContainerMenu {
  public final ATMBlockEntity blockEntity;
  public final Level level;
  public final Player player;

  public static int CARD_SLOT = 0;
  public static int DEPOSIT_SLOT = 1;

  public final ItemStackHandler inventory = new ItemStackHandler(2) {
    protected int getStackLimit(int slot, net.minecraft.world.item.ItemStack stack) {
      return 1;
    };

    protected void onContentsChanged(int slot) {
      deposit();
    };

    public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
      if (slot == CARD_SLOT) {
        return stack.getItem() instanceof CardItem;
      }

      ItemStack cardStack = inventory.getStackInSlot(CARD_SLOT);
      if (cardStack.isEmpty() || !(cardStack.getItem() instanceof CardItem)) {
        player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);
        return false;
      }

      if (slot == DEPOSIT_SLOT) {
        return stack.getItem() instanceof MoneyItem;
      }

      return super.isItemValid(slot, stack);
    };

  };

  public ATMMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
    this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
  }

  @Override
  public void removed(Player player) {
    this.setCarried(inventory.getStackInSlot(CARD_SLOT));
    super.removed(player);
  }

  public ATMMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
    super(Menus.ATM_MENU.get(), containerId);
    if (!(blockEntity instanceof ATMBlockEntity atm)) {
      throw new IllegalStateException("Block entity is not an ATMBlockEntity");
    }
    this.blockEntity = atm;
    this.level = inv.player.level();
    this.player = inv.player;

    addPlayerInventory(inv);
    addPlayerHotbar(inv);

    this.addSlot(new SlotItemHandler(this.inventory, CARD_SLOT, 80, 24));
    this.addSlot(new SlotItemHandler(this.inventory, DEPOSIT_SLOT, 80, 48));

    if (player instanceof ServerPlayer serverPlayer) {
      var money = player.getData(DataAttachment.MONEY);
      PacketDistributor.sendToPlayer(serverPlayer, new MoneyData(money));
    }
  }

  private void deposit() {
    ItemStack cardStack = inventory.getStackInSlot(CARD_SLOT);
    if (cardStack.isEmpty() || !(cardStack.getItem() instanceof CardItem)) {
      return;
    }
    ItemStack depositStack = inventory.getStackInSlot(DEPOSIT_SLOT);

    if (!depositStack.isEmpty() && depositStack.getItem() instanceof MoneyItem item) {
      var amount = item.getValue() * depositStack.getCount();
      inventory.setStackInSlot(DEPOSIT_SLOT, ItemStack.EMPTY);

      var money = player.getData(DataAttachment.MONEY);
      player.setData(DataAttachment.MONEY, money + amount);

      player.playNotifySound(SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 1.0f, 1.0f);

      if (player instanceof ServerPlayer serverPlayer)
        PacketDistributor.sendToPlayer(serverPlayer, new MoneyData(money + amount));
    }
  }

  public void withdraw(int money, boolean shift) {
    ItemStack cardStack = inventory.getStackInSlot(CARD_SLOT);
    if (cardStack.isEmpty() || !(cardStack.getItem() instanceof CardItem)) {
      player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);
      return;
    }

    PacketDistributor.sendToServer(new ATMWithdraw(money, shift ? 64 : 1));
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, Blocks.ATM.get());
  }

  @Override
  public void slotsChanged(Container changedContainer) {
    super.slotsChanged(changedContainer);
    deposit();
  }

  private void addPlayerInventory(Inventory inv) {
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
      }
    }
  }

  private void addPlayerHotbar(Inventory inv) {
    for (int col = 0; col < 9; ++col) {
      this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
    }
  }

  // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
  private static final int HOTBAR_SLOT_COUNT = 9;
  private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
  private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
  private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
  private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
  private static final int VANILLA_FIRST_SLOT_INDEX = 0;
  private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 2; // must be the number of slots you have!

  @Override
  public ItemStack quickMoveStack(Player playerIn, int pIndex) {
    Slot sourceSlot = slots.get(pIndex);
    if (sourceSlot == null || !sourceSlot.hasItem())
      return ItemStack.EMPTY; // EMPTY_ITEM
    ItemStack sourceStack = sourceSlot.getItem();
    ItemStack copyOfSourceStack = sourceStack.copy();

    // Check if the slot clicked is one of the vanilla container slots
    if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
      // This is a vanilla container slot so merge the stack into the tile inventory
      if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
          + TE_INVENTORY_SLOT_COUNT, false)) {
        return ItemStack.EMPTY; // EMPTY_ITEM
      }
    } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
      // This is a TE slot so merge the stack into the players inventory
      if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
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
