package fr.kevyn.economy.block;

import fr.kevyn.economy.block.entity.ATMBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ATMBlock extends Block implements EntityBlock {
  private VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 16, 32, 16));
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

  public ATMBlock() {
    super(BlockBehaviour.Properties.of()
        .sound(SoundType.METAL)
        .strength(1f, 10f)
        .noOcclusion()
        .isRedstoneConductor((bs, br, bp) -> false));
    this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.POWERED, false));
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
    super.createBlockStateDefinition(builder);
    builder.add(
        FACING,
        BlockStateProperties.POWERED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return this.SHAPE;
  }

  @Override
  protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return this.SHAPE;
  }

  // emit redstone on right click

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
      BlockHitResult hitResult) {
    if (!level.isClientSide) {
      level.scheduleTick(pos, this, 10); // schedule block tick in 1 tick
      level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 3);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (state.getValue(BlockStateProperties.POWERED)) {
      level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 3);
    }
  }

  @Override
  public boolean isSignalSource(BlockState state) {
    return true;
  }

  @Override
  public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
  }
}
