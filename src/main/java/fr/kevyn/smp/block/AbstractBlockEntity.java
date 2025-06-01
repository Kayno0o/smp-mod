package fr.kevyn.smp.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBlockEntity extends BlockEntity implements MenuProvider {
  protected AbstractBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
  }

  @Override
  public Component getDisplayName() {
    return Component.literal("displayName");
  }

  public SimpleMenuProvider getMenuProvider() {
    return new SimpleMenuProvider(this, getDisplayName());
  }
}
