package fr.kevyn.artisanspath.block;

import fr.kevyn.artisanspath.init.ArtisansBlockEntities;
import fr.kevyn.artisanspath.ui.menu.ATMMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ATMBlockEntity extends AbstractBlockEntity {
  private int color = 0xFFFFFFFF;

  public ATMBlockEntity(BlockPos pos, BlockState state) {
    super(ArtisansBlockEntities.ATM.get(), pos, state);
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new ATMMenu(i, inventory, this);
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
    setChanged();
  }

  @Override
  public void setChanged() {
    super.setChanged();

    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putInt("color", color);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    color = tag.getInt("color");
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = new CompoundTag();
    saveAdditional(tag, registries);
    return tag;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
