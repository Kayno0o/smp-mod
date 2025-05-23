package fr.kevyn.smp.mixin.farmersdelight;

import com.alessandro.astages.core.ARestrictionManager;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin {
  @Inject(method = "getMatchingRecipe", at = @At("RETURN"), cancellable = true)
  private void injectGetMatchingRecipe(
      RecipeWrapper inventoryWrapper,
      CallbackInfoReturnable<Optional<RecipeHolder<CookingPotRecipe>>> cir) {
    var recipe = cir.getReturnValue();

    recipe.ifPresent(r -> {
      BlockEntity self = (BlockEntity) (Object) this;
      var level = self.getLevel();
      if (level == null) return;

      var server = level.getServer();
      if (server == null) return;

      if (!self.getPersistentData().hasUUID("smp:owner")) return;

      Player player =
          server.getPlayerList().getPlayer(self.getPersistentData().getUUID("smp:owner"));
      if (player == null) return;

      @SuppressWarnings("null")
      ItemStack stack = r.value().getResultItem(null);

      var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(
          player,
          new com.alessandro.astages.core.wrapper.RecipeWrapper(
              RecipeType.simple(ResourceLocation.fromNamespaceAndPath("farmersdelight", "cooking")),
              ResourceLocation.parse(stack.getItem().toString())));

      if (restriction != null) cir.setReturnValue(Optional.empty());
    });
  }
}
