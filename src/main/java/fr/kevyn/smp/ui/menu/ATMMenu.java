package fr.kevyn.smp.ui.menu;

import fr.kevyn.smp.block.ATMBlockEntity;
import fr.kevyn.smp.init.SmpBlocks;
import fr.kevyn.smp.init.SmpMenus;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.item.MoneyItem;
import fr.kevyn.smp.network.server.ATMWithdrawNet;
import fr.kevyn.smp.ui.screen.ATMScreen;
import fr.kevyn.smp.utils.AccountUtils;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class ATMMenu extends AbstractBlockEntityMenu<ATMMenu, ATMBlockEntity> {
  public static final int CARD_SLOT = 0;
  public static final int DEPOSIT_SLOT = 1;

  public final ItemStackHandler inventory = new ItemStackHandler(2) {
    @Override
    protected int getStackLimit(int slot, net.minecraft.world.item.ItemStack stack) {
      return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
      deposit();
    }

    @Override
    public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
      if (slot == CARD_SLOT) {
        return stack.getItem() instanceof CardItem;
      }

      ItemStack cardStack = inventory.getStackInSlot(CARD_SLOT);
      if (cardStack.isEmpty() || !(cardStack.getItem() instanceof CardItem)) {
        return false;
      }

      if (slot == DEPOSIT_SLOT) {
        return stack.getItem() instanceof MoneyItem;
      }

      return super.isItemValid(slot, stack);
    }
  };

  public ATMMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
    this(id, inv, castBlockEntity(inv.player.level().getBlockEntity(extraData.readBlockPos())));
  }

  private static ATMBlockEntity castBlockEntity(BlockEntity be) {
    if (!(be instanceof ATMBlockEntity atm)) {
      throw new IllegalStateException("Block entity is not an ATMBlockEntity");
    }
    return atm;
  }

  public ATMMenu(int containerId, Inventory inv, ATMBlockEntity atm) {
    super(SmpMenus.ATM_MENU.get(), containerId, atm, inv.player, 2);

    addPlayerInventory(inv, ATMScreen.HEIGHT);
    addPlayerHotbar(inv, ATMScreen.HEIGHT);

    addSlot(new SlotItemHandler(inventory, DEPOSIT_SLOT, 152, 29));
    addSlot(new SlotItemHandler(inventory, CARD_SLOT, 152, 51));
  }

  @Override
  public void removed(Player player) {
    this.setCarried(inventory.getStackInSlot(CARD_SLOT));
    super.removed(player);
  }

  private void deposit() {
    if (level == null || level.isClientSide() || !(this.player instanceof ServerPlayer)) return;

    var player = (ServerPlayer) this.player;

    ItemStack cardStack = inventory.getStackInSlot(CARD_SLOT);
    if (cardStack.isEmpty() || !(cardStack.getItem() instanceof CardItem)) return;

    ItemStack depositStack = inventory.getStackInSlot(DEPOSIT_SLOT);

    if (!depositStack.isEmpty() && depositStack.getItem() instanceof MoneyItem item) {
      var amount = item.getValue() * depositStack.getCount();
      inventory.setStackInSlot(DEPOSIT_SLOT, ItemStack.EMPTY);

      if (!AccountUtils.hasAccessToAccount(cardStack, player, level)) return;

      UUID accountId = AccountUtils.getAccountUUID(cardStack);
      if (AccountUtils.addMoney(accountId, player, amount)) {
        player.playNotifySound(
            SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
      }
    }
  }

  public void withdraw(int money, boolean shift) {
    ItemStack cardStack = inventory.getStackInSlot(CARD_SLOT);
    if (cardStack.isEmpty() || !(cardStack.getItem() instanceof CardItem)) {
      player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);
      return;
    }

    var account = AccountUtils.getAccountUUID(cardStack);
    if (account == null) {
      player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);
      return;
    }

    PacketDistributor.sendToServer(new ATMWithdrawNet(account, money, shift ? 64 : 1));
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(
        ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, SmpBlocks.ATM.get());
  }

  @Override
  public void slotsChanged(Container changedContainer) {
    super.slotsChanged(changedContainer);
    deposit();
  }
}
