package fr.kevyn.smp.atm;

import fr.kevyn.smp.init.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ATMBlockEntity extends BlockEntity implements MenuProvider {
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

  // @Override
  // protected void saveAdditional(CompoundTag tag, HolderLookup.Provider
  // registries) {
  // super.saveAdditional(tag, registries);
  // tag.put("inventory", inventory.serializeNBT(registries));
  // }

  // @Override
  // protected void loadAdditional(CompoundTag tag, HolderLookup.Provider
  // registries) {
  // super.loadAdditional(tag, registries);
  // inventory.deserializeNBT(registries, tag.getCompound("inventory"));
  // }
}
