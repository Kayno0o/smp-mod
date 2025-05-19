package fr.kevyn.smp.atm;

import fr.kevyn.smp.init.BlockEntities;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.item.MoneyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ATMBlockEntity extends BlockEntity implements MenuProvider {
  public static int CARD_SLOT = 0;
  public static int DEPOSIT_SLOT = 1;

  public final ItemStackHandler inventory = new ItemStackHandler(2) {
    protected int getStackLimit(int slot, net.minecraft.world.item.ItemStack stack) {
      return 1;
    };

    protected void onContentsChanged(int slot) {
      setChanged();
    };

    public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
      if (slot == CARD_SLOT) {
        return stack.getItem() instanceof CardItem;
      }

      if (slot == DEPOSIT_SLOT) {
        return stack.getItem() instanceof MoneyItem;
      }

      return super.isItemValid(slot, stack);
    };

  };

  public ATMBlockEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.ATM.get(), pos, state);
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new ATMMenu(i, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.literal("ATM");
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.put("inventory", inventory.serializeNBT(registries));
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    inventory.deserializeNBT(registries, tag.getCompound("inventory"));
  }
}
