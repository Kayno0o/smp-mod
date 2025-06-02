package fr.kevyn.artisanspath.block;

import fr.kevyn.artisanspath.item.ATMBlockItem;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

public class ATMBlock extends Block implements EntityBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

  public ATMBlock() {
    super(BlockBehaviour.Properties.of()
        .sound(SoundType.METAL)
        .strength(1f, 10f)
        .noOcclusion()
        .isRedstoneConductor((bs, br, bp) -> false));
  }

  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ATMBlockEntity(pos, state);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return false;
  }

  @Override
  protected boolean useShapeForLightOcclusion(BlockState state) {
    return false;
  }

  @Override
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 0;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
  }

  @Override
  public void setPlacedBy(
      Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);

    // Apply color from item to block entity
    if (level.getBlockEntity(pos) instanceof ATMBlockEntity atmBlockEntity) {
      DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
      if (dyedColor != null) {
        atmBlockEntity.setColor(dyedColor.rgb());
      }
    }
  }

  @Override
  protected ItemInteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hitResult) {
    if (level.getBlockEntity(pos) instanceof ATMBlockEntity atmBlockEntity
        && !level.isClientSide()) {
      if (stack.getItem() instanceof DyeItem dyeItem) {
        int dyeColor = dyeItem.getDyeColor().getTextureDiffuseColor();
        atmBlockEntity.setColor(dyeColor);

        if (!player.getAbilities().instabuild) {
          stack.shrink(1);
        }

        return ItemInteractionResult.SUCCESS;
      }

      if (player instanceof ServerPlayer serverPlayer) {
        serverPlayer.openMenu(atmBlockEntity.getMenuProvider(), pos);
        return ItemInteractionResult.SUCCESS;
      }
    }

    return ItemInteractionResult.SUCCESS;
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
    List<ItemStack> drops = super.getDrops(state, params);

    // Add color to dropped item
    if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof ATMBlockEntity atm) {
      for (ItemStack drop : drops) {
        if (drop.getItem() instanceof ATMBlockItem) {
          int color = atm.getColor();
          if (color != ATMBlockItem.DEFAULT_COLOR) {
            drop.set(DataComponents.DYED_COLOR, new DyedItemColor(color, false));
          }
        }
      }
    }

    return drops;
  }
}
