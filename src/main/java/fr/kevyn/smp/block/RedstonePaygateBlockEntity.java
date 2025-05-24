package fr.kevyn.smp.block;

import fr.kevyn.smp.init.SmpBlockEntities;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.item.CardItem;
import fr.kevyn.smp.network.client.UpdateMoneyNet;
import fr.kevyn.smp.ui.menu.RedstonePaygateMenu;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class RedstonePaygateBlockEntity extends AbstractBlockEntity {
  public static final int CARD_SLOT = 0;
  public static final int MAX_BALANCE = 100;

  @Nullable private UUID ownerId;

  private int price = 0;
  private int balance = 0;

  public final ItemStackHandler inventory = new ItemStackHandler(1) {
    protected int getStackLimit(int slot, ItemStack stack) {
      return 1;
    }

    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == CARD_SLOT) {
        return stack.getItem() instanceof CardItem;
      }
      return super.isItemValid(slot, stack);
    }

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
    int money = player.getData(SmpDataAttachments.MONEY);

    player.setData(SmpDataAttachments.MONEY, money + this.balance);
    PacketDistributor.sendToPlayer(player, new UpdateMoneyNet(money + this.balance));

    this.setBalance(0);
    this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
  }

  public boolean pay(ServerPlayer player) {
    if (this.ownerId == null) return false;

    if (level != null) {
      if (level.getBlockState(this.worldPosition) instanceof BlockState bs)
        if (bs.getValue(RedstonePaygateBlock.POWERED) == true) return false;

      var owner = ((ServerPlayer) level.getPlayerByUUID(this.ownerId));
      if (owner == null) return false;

      int money = player.getData(SmpDataAttachments.MONEY);
      if (money < this.price) return false;

      if (!(this.inventory.getStackInSlot(CARD_SLOT).getItem() instanceof CardItem)) {
        if (this.price > MAX_BALANCE - this.balance) {
          level.playSound(
              player, player.getOnPos(), SoundEvents.VILLAGER_NO, SoundSource.BLOCKS, 1f, 0f);

          return false;
        }

        level.playSound(
            player, player.getOnPos(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1f, 1f);

        player.setData(SmpDataAttachments.MONEY, money - price);
        PacketDistributor.sendToPlayer(player, new UpdateMoneyNet(money - price));

        this.setBalance(this.balance + this.price);

        return true;
      }

      player.setData(SmpDataAttachments.MONEY, money - price);
      PacketDistributor.sendToPlayer(player, new UpdateMoneyNet(money - price));

      int ownerMoney = player.getData(SmpDataAttachments.MONEY);
      owner.setData(SmpDataAttachments.MONEY, ownerMoney + price);
      PacketDistributor.sendToPlayer(owner, new UpdateMoneyNet(ownerMoney + price));

      level.playSound(
          player, player.getOnPos(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1f, 1f);

      return true;
    }

    return false;
  }
}
