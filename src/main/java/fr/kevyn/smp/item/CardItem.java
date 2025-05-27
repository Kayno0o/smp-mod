package fr.kevyn.smp.item;

import fr.kevyn.smp.component.LocalAccountEntry;
import fr.kevyn.smp.utils.AccountUtils;
import fr.kevyn.smp.utils.NumberUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
    if (player.isCrouching() && player instanceof ServerPlayer serverPlayer) {
      ItemStack stack = serverPlayer.getItemInHand(usedHand);
      if (AccountUtils.getAccountUUID(stack) != null) {
        AccountUtils.clearAccount(stack);
        level.playSound(
            serverPlayer,
            serverPlayer.getOnPos(),
            SoundEvents.GLASS_BREAK,
            SoundSource.BLOCKS,
            1f,
            0f);
      } else {
        AccountUtils.setAccount(stack, serverPlayer.getUUID(), (ServerLevel) level);
        level.playSound(
            serverPlayer,
            serverPlayer.getOnPos(),
            SoundEvents.EXPERIENCE_ORB_PICKUP,
            SoundSource.BLOCKS,
            1f,
            1f);
      }
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

    LocalAccountEntry account = AccountUtils.getAccount(stack, player);
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
