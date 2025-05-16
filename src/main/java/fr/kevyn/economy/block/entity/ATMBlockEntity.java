package fr.kevyn.economy.block.entity;

import fr.kevyn.economy.init.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ATMBlockEntity extends BlockEntity {
  public ATMBlockEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.ATM.get(), pos, state);
  }
}
