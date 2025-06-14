package fr.kevyn.artisanspath.item;

import fr.kevyn.artisanspath.projectile.CoinProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoinItem extends MoneyItem {
  public CoinItem() {
    super(1);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);

    if (!level.isClientSide()) {
      CoinProjectile coin = new CoinProjectile(level, player);
      coin.setItem(stack);
      coin.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0.2F);
      level.addFreshEntity(coin);
      if (!player.getAbilities().instabuild) {
        stack.shrink(1);
      }
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
  }
}
