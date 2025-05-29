package fr.kevyn.smp.item;

import fr.kevyn.smp.data.AccountEntry;
import fr.kevyn.smp.init.SmpDataAttachments;
import fr.kevyn.smp.ui.menu.AccountSelectionMenu;
import fr.kevyn.smp.ui.screen.AccountSelectionScreen;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.NumberUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CardItem extends Item {
  public CardItem() {
    super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
  }

  @Override
  public void onCraftedBy(ItemStack stack, Level level, Player player) {
    super.onCraftedBy(stack, level, player);

    if (level instanceof ServerLevel serverLevel)
      AccountUtils.setAccount(stack, player.getUUID(), serverLevel);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(
      Level level, Player player, InteractionHand usedHand) {
    if (level.isClientSide()
        && player instanceof LocalPlayer localPlayer
        && localPlayer.hasData(SmpDataAttachments.ACCOUNTS)) {
      var accounts = localPlayer.getData(SmpDataAttachments.ACCOUNTS);

      UUID currentAccount = AccountUtils.getAccountUUID(localPlayer.getItemInHand(usedHand));

      AccountSelectionMenu menu = new AccountSelectionMenu(0, localPlayer.getInventory());

      Minecraft.getInstance()
          .setScreen(new AccountSelectionScreen(
              menu,
              localPlayer.getInventory(),
              Component.literal("Account selection"),
              usedHand,
              new ArrayList<>(accounts.values()),
              currentAccount));
    }
    return super.use(level, player, usedHand);
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      List<Component> tooltipComponents,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

    LocalPlayer player = Minecraft.getInstance().player;
    if (player == null) return;

    AccountEntry account = AccountUtils.getLocalAccount(stack, player);
    if (account == null) return;

    tooltipComponents.add(Component.literal("Account: ")
        .withStyle(ChatFormatting.GRAY)
        .append(Component.literal(account.name()).withStyle(ChatFormatting.BLUE)));

    tooltipComponents.add(Component.literal("Balance: ")
        .withStyle(ChatFormatting.GRAY)
        .append(Component.literal(NumberUtils.CURRENCY_FORMAT.format(account.money()))
            .withStyle(ChatFormatting.YELLOW)));
  }
}
