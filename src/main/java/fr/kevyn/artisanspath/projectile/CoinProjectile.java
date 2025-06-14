package fr.kevyn.artisanspath.projectile;

import fr.kevyn.artisanspath.init.ArtisansEntities;
import fr.kevyn.artisanspath.init.ArtisansItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CoinProjectile extends ThrowableItemProjectile {
  public CoinProjectile(EntityType<? extends CoinProjectile> type, Level level) {
    super(type, level);
  }

  public CoinProjectile(Level level, LivingEntity owner) {
    super(ArtisansEntities.COIN_PROJECTILE.get(), owner, level);
  }

  @Override
  protected Item getDefaultItem() {
    return ArtisansItems.COIN.get();
  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    result.getEntity().hurt(this.level().damageSources().thrown(this, this.getOwner()), 1.0F);
  }

  @Override
  protected void onHit(HitResult result) {
    super.onHit(result);

    if (!this.level().isClientSide) {
      ItemEntity drop = new ItemEntity(
          this.level(),
          this.getX(),
          this.getY(),
          this.getZ(),
          new ItemStack(ArtisansItems.COIN.get()));
      this.level().addFreshEntity(drop);
      this.discard();
    }
  }

  @Override
  protected double getDefaultGravity() {
    return 0.04;
  }
}
