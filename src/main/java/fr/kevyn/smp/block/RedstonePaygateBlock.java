package fr.kevyn.smp.block;

import fr.kevyn.smp.item.CardItem;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class RedstonePaygateBlock extends Block implements EntityBlock {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final DirectionProperty FACING = BlockStateProperties.FACING;

  public RedstonePaygateBlock() {
    super(BlockBehaviour.Properties.of()
        .sound(SoundType.METAL)
        .strength(1f, 10f)
        .noOcclusion()
        .isRedstoneConductor((bs, br, bp) -> false));

    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
  }

  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new RedstonePaygateBlockEntity(pos, state);
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
    builder.add(POWERED);
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
  }

  @Override
  public boolean isSignalSource(BlockState state) {
    return true;
  }

  @Override
  public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    return state.getValue(POWERED) ? 15 : 0;
  }

  public void activate(Level level, BlockState state, BlockPos pos) {
    if (!level.isClientSide && !state.getValue(POWERED)) {
      level.setBlock(pos, state.setValue(POWERED, true), 3);
      level.updateNeighborsAt(pos, this);
      level.scheduleTick(pos, this, 20);
    }
  }

  @Override
  public void setPlacedBy(
      Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);
    var blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof RedstonePaygateBlockEntity paygate
        && placer instanceof Player player) {
      paygate.setOwnerId(player.getUUID());
    }
  }

  @Override
  public boolean onDestroyedByPlayer(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      boolean willHarvest,
      FluidState fluid) {
    if (level.getBlockEntity(pos) instanceof RedstonePaygateBlockEntity paygate)
      if (!player.getUUID().equals(paygate.getOwnerId())) return false;

    return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
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
    if (level.getBlockEntity(pos) instanceof RedstonePaygateBlockEntity paygate) {
      if (!level.isClientSide()) {

        if (!stack.isEmpty() && stack.getItem() instanceof CardItem) {
          if (
          /*player.getUUID().equals(paygate.getOwnerId()) ||*/
          paygate.withdraw((ServerPlayer) player)) {
            this.activate(level, state, pos);
          }

          return ItemInteractionResult.SUCCESS;
        }

        if (player.getUUID().equals(paygate.getOwnerId()))
          ((ServerPlayer) player).openMenu(paygate.getMenuProvider(), pos);
      }
    }

    return ItemInteractionResult.SUCCESS;
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (state.getValue(POWERED)) {
      level.setBlock(pos, state.setValue(POWERED, false), 3);
      level.updateNeighborsAt(pos, this);
    }
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.BLOCK;
  }

  @Override
  public float getExplosionResistance() {
    return 3600000.0F;
  }

  @Override
  public boolean canEntityDestroy(
      BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
    return false;
  }

  @Override
  public boolean skipRendering(BlockState oldState, BlockState newState, Direction direction) {
    return oldState.getBlock() == newState.getBlock();
  }

  @Override
  public void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity be = level.getBlockEntity(pos);

      if (be instanceof RedstonePaygateBlockEntity paygate) {
        NonNullList<ItemStack> drops =
            NonNullList.withSize(paygate.inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < drops.size(); i++) {
          drops.set(i, paygate.inventory.getStackInSlot(i));
        }
        Containers.dropContents(level, pos, drops);
        level.updateNeighbourForOutputSignal(pos, this);
      }
    }

    super.onRemove(state, level, pos, newState, isMoving);
  }
}
