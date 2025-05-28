package fr.kevyn.smp.block;

import fr.kevyn.smp.init.SmpBlockEntities;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.ui.menu.RedstonePaygateMenu;
import fr.kevyn.smp.utils.AccountUtils;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class RedstonePaygateBlockEntity extends AbstractBlockEntity {
  public static final int CARD_SLOT = 0;
  public static final int MAX_BALANCE = 100;

  @Nullable private UUID ownerId;

  private int price = 0;
  private int balance = 0;

  public final ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == CARD_SLOT) {
        return stack.getItem() instanceof CardItem;
      }
      return super.isItemValid(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
    }
  };

  public RedstonePaygateBlockEntity(BlockPos pos, BlockState state) {
    super(SmpBlockEntities.REDSTONE_PAYGATE.get(), pos, state, "Redstone Paygate");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new RedstonePaygateMenu(i, inventory, this);
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

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);

    tag.put("inventory", inventory.serializeNBT(registries));

    if (ownerId != null) tag.putUUID("owner", ownerId);

    tag.putInt("price", price);
    tag.putInt("balance", balance);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);

    inventory.deserializeNBT(registries, tag.getCompound("inventory"));

    if (tag.contains("owner")) ownerId = tag.getUUID("owner");

    price = tag.getInt("price");
    balance = tag.getInt("balance");
  }

  public int getPrice() {
    return this.price;
  }

  public void setPrice(int price) {
    this.price = price;
    this.setChanged();
  }

  public UUID getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(UUID ownerId) {
    this.ownerId = ownerId;
    this.setChanged();
  }

  public int getBalance() {
    return this.balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
    this.setChanged();
  }

  public void withdraw(ServerPlayer player) {
    if (!(this.inventory.getStackInSlot(CARD_SLOT).getItem() instanceof CardItem)) return;

    var blockOwner = ((ServerPlayer) level.getPlayerByUUID(this.ownerId));
    if (blockOwner == null || !player.getUUID().equals(blockOwner.getUUID())) return;

    var blockAccount = AccountUtils.getAccount(
        player.serverLevel(), this.inventory.getStackInSlot(RedstonePaygateBlockEntity.CARD_SLOT));
    if (blockAccount == null) return;

    AccountUtils.addMoney(blockAccount, blockOwner, this.balance);

    this.setBalance(0);
    this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
  }

  public boolean pay(ServerPlayer player) {
    if (this.ownerId == null) return false;

    if (level != null) {
      if (level.getBlockState(this.worldPosition) instanceof BlockState bs
          && Boolean.TRUE.equals(bs.getValue(RedstonePaygateBlock.POWERED))) return false;

      var blockOwner = ((ServerPlayer) level.getPlayerByUUID(this.ownerId));
      if (blockOwner == null) return false;

      var playerAccount =
          AccountUtils.getAccount(level, player.getItemInHand(InteractionHand.MAIN_HAND));
      if (playerAccount == null) return false;

      if (!(this.inventory.getStackInSlot(CARD_SLOT).getItem() instanceof CardItem)) {
        if (this.price > MAX_BALANCE - this.balance) {
          level.playSound(
              player, player.getOnPos(), SoundEvents.VILLAGER_NO, SoundSource.BLOCKS, 1f, 0f);

          return false;
        }

        level.playSound(
            player, player.getOnPos(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1f, 1f);

        AccountUtils.addMoney(playerAccount, player, -price);

        this.setBalance(this.balance + this.price);

        return true;
      }

      var blockAccount = AccountUtils.getAccount(
          level, this.inventory.getStackInSlot(RedstonePaygateBlockEntity.CARD_SLOT));
      if (blockAccount == null || !AccountUtils.hasAccessToAccount(blockAccount, blockOwner))
        return false;

      if (!AccountUtils.addMoney(playerAccount, player, -this.price)) return false;

      AccountUtils.addMoney(blockAccount, blockOwner, this.price);

      level.playSound(
          player, player.getOnPos(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1f, 1f);

      return true;
    }

    return false;
  }
}
