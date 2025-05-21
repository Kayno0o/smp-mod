package fr.kevyn.smp.mixin.farmersdelight;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.alessandro.astages.core.ARestrictionManager;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin {
  @Shadow
  private RecipeManager.CachedCheck<RecipeWrapper, CookingPotRecipe> quickCheck;

  @Shadow
  protected abstract boolean hasInput();

  @Inject(method = "getMatchingRecipe", at = @At("RETURN"), cancellable = true)
  private void injectGetMatchingRecipe(RecipeWrapper inventoryWrapper,
      CallbackInfoReturnable<Optional<RecipeHolder<CookingPotRecipe>>> cir) {
    CookingPotBlockEntity self = (CookingPotBlockEntity) (Object) this;
    var level = self.getLevel();

    if (level != null && this.hasInput()) {
      var recipe = this.quickCheck.getRecipeFor(inventoryWrapper, level);
      recipe.ifPresent(r -> {
        CookingPotRecipe instance = r.value();
        @SuppressWarnings("null")
        ItemStack stack = instance.getResultItem(null);

        var server = level.getServer();
        if (server == null)
          return;

        if (!self.getPersistentData().hasUUID("smp:owner"))
          return;

        Player player = server.getPlayerList().getPlayer(self.getPersistentData().getUUID("smp:owner"));
        if (player == null)
          return;

        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player,
            new com.alessandro.astages.core.wrapper.RecipeWrapper(
                RecipeType.simple(ResourceLocation.fromNamespaceAndPath("farmersdelight", "cooking")),
                ResourceLocation.parse(stack.getItem().toString())));

        if (restriction != null)
          cir.setReturnValue(Optional.empty());
      });
    }
  }
}
