package fr.kevyn.smp.ui.menu;

import fr.kevyn.smp.block.RedstonePaygateBlockEntity;
import fr.kevyn.smp.init.SmpBlocks;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.init.SmpMenus;
import fr.kevyn.smp.network.client.UpdateMoneyNet;
import fr.kevyn.smp.network.server.MenuActionNet;
import fr.kevyn.smp.ui.screen.RedstonePaygateScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class RedstonePaygateMenu
    extends AbstractMenu<RedstonePaygateMenu, RedstonePaygateBlockEntity>
    implements IMenuActionHandler {
  public static String ACTION_SET_PRICE = "set_price";
  public static String ACTION_WITHDRAW = "withdraw";

  public RedstonePaygateMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
    this(id, inv, castBlockEntity(inv.player.level().getBlockEntity(extraData.readBlockPos())));
  }

  private static RedstonePaygateBlockEntity castBlockEntity(BlockEntity be) {
    if (!(be instanceof RedstonePaygateBlockEntity atm)) {
      throw new IllegalStateException("Block entity is not an RedstonePaygateBlockEntity");
    }
    return atm;
  }

  public RedstonePaygateMenu(
      int containerId, Inventory inv, RedstonePaygateBlockEntity blockEntity) {
    super(SmpMenus.REDSTONE_PAYGATE_MENU.get(), containerId, blockEntity, inv.player, 1);

    addPlayerInventory(inv, RedstonePaygateScreen.HEIGHT);
    addPlayerHotbar(inv, RedstonePaygateScreen.HEIGHT);

    this.addSlot(
        new SlotItemHandler(blockEntity.inventory, RedstonePaygateBlockEntity.CARD_SLOT, 80, 26));

    if (player instanceof ServerPlayer serverPlayer) {
      var money = player.getData(SmpDataAttachments.MONEY);
      PacketDistributor.sendToPlayer(serverPlayer, new UpdateMoneyNet(money));
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(
        ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
        player,
        SmpBlocks.REDSTONE_PAYGATE.get());
  }

  @Override
  public String getMenuIdentifier() {
    return "redstone_paygate";
  }

  @Override
  public void handleMenuAction(MenuActionNet action) {
    if (action.action().equals(ACTION_SET_PRICE)) {
      blockEntity.setPrice(action.amount());
      return;
    }

    if (action.action().equals(ACTION_WITHDRAW)) {
      if (player instanceof ServerPlayer serverPlayer) blockEntity.withdraw(serverPlayer);
      return;
    }
  }
}
