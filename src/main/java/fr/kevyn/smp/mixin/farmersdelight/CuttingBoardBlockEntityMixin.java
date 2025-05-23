package fr.kevyn.smp.mixin.farmersdelight;

import com.alessandro.astages.core.ARestrictionManager;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;

@Mixin(CuttingBoardBlockEntity.class)
public class CuttingBoardBlockEntityMixin {
  @Shadow
  private RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;

  @Inject(method = "getMatchingRecipe", at = @At("RETURN"), cancellable = true)
  private void getMatchingRecipe(
      ItemStack toolStack,
      @Nullable Player player,
      CallbackInfoReturnable<Optional<RecipeHolder<CuttingBoardRecipe>>> cir) {
    CuttingBoardBlockEntity self = (CuttingBoardBlockEntity) (Object) this;
    var level = self.getLevel();
    if (level == null) return;

    Optional<RecipeHolder<CuttingBoardRecipe>> recipe = this.quickCheck.getRecipeFor(
        new CuttingBoardRecipeInput(self.getStoredItem(), toolStack), level);

    recipe.ifPresent(r -> {
      CuttingBoardRecipe instance = r.value();
      @SuppressWarnings("null")
      ItemStack stack = instance.getResultItem(null);

      var server = level.getServer();
      if (server == null) return;

      var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(
          player,
          new com.alessandro.astages.core.wrapper.RecipeWrapper(
              RecipeType.simple(ResourceLocation.fromNamespaceAndPath("farmersdelight", "cutting")),
              ResourceLocation.parse(stack.getItem().toString())));

      if (restriction != null) cir.setReturnValue(Optional.empty());
    });
  }
}
