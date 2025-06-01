package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.data.AccountEntry;
import fr.kevyn.artisanspath.init.ArtisansDataAttachments;
import fr.kevyn.artisanspath.item.handler.ClientCardHandler;
import fr.kevyn.artisanspath.utils.AccountUtils;
import fr.kevyn.artisanspath.utils.NumberUtils;
import java.util.List;
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

public class PaymentCardItem extends Item {
  public PaymentCardItem() {
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
        && localPlayer.hasData(ArtisansDataAttachments.ACCOUNTS)) {
      ClientCardHandler.openSelectionScreen(localPlayer, usedHand);
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

    tooltipComponents.add(Component.translatable(
            "tooltip.artisanspath.payment_card.account",
            Component.literal(account.name()).withStyle(ChatFormatting.BLUE))
        .withStyle(ChatFormatting.GRAY));

    tooltipComponents.add(Component.translatable(
            "tooltip.artisanspath.payment_card.balance",
            Component.literal(NumberUtils.getCurrencyFormat().format(account.money()))
                .withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY));
  }
}
