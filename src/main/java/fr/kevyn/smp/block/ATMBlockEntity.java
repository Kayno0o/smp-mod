package fr.kevyn.smp.block;

import fr.kevyn.smp.init.SmpBlockEntities;
import fr.kevyn.smp.ui.menu.ATMMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ATMBlockEntity extends AbstractBlockEntity {
  public ATMBlockEntity(BlockPos pos, BlockState state) {
    super(SmpBlockEntities.ATM.get(), pos, state, "ATM");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new ATMMenu(i, inventory, this);
  }
}
