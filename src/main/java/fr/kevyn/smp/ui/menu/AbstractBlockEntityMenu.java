package fr.kevyn.smp.ui.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractBlockEntityMenu<
        T extends AbstractContainerMenu, B extends BlockEntity>
    extends AbstractMenu<T> {
  public final B blockEntity;
  public final Level level;

  protected AbstractBlockEntityMenu(
      MenuType<T> menuType, int containerId, B blockEntity, Player player, int slotsCount) {
    super(menuType, containerId, player, slotsCount);
    this.blockEntity = blockEntity;
    this.level = blockEntity.getLevel();
  }
}
