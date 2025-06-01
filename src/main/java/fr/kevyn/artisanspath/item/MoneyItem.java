package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.utils.NumberUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

public class MoneyItem extends Item {
  private final int value;

  public MoneyItem(int value) {
    super(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      List<Component> tooltipComponents,
      TooltipFlag tooltipFlag) {
    tooltipComponents.add(Component.translatable(
            "tooltip.artisanspath.value", NumberUtils.getCurrencyFormat().format(this.value))
        .withStyle(ChatFormatting.GRAY));

    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
  }
}
