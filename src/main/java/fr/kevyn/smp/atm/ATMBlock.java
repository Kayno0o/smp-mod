package fr.kevyn.smp.atm;

import fr.kevyn.smp.item.CardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.neoforged.neoforge.items.ItemStackHandler;

public class ATMBlock extends Block implements EntityBlock {
  private VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 16, 32, 16));
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
    super.createBlockStateDefinition(builder);
    builder.add(FACING);
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

  @Override
  protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
    return this.SHAPE;
  }

  @Override
  protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult hitResult) {
    if (level.getBlockEntity(pos) instanceof ATMBlockEntity atmBlockEntity) {
      ItemStack cardSlotStack = atmBlockEntity.inventory.getStackInSlot(ATMBlockEntity.CARD_SLOT);

      // drop card into ATM
      if (cardSlotStack.isEmpty() && !stack.isEmpty() && stack.getItem() instanceof CardItem) {
        atmBlockEntity.inventory.insertItem(ATMBlockEntity.CARD_SLOT, stack.copy(), false);
        stack.shrink(1);
        level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
      }

      if (!level.isClientSide()) {
        ((ServerPlayer) player).openMenu(new SimpleMenuProvider(atmBlockEntity, Component.literal("ATM")), pos);
      }
    }
    return ItemInteractionResult.SUCCESS;
  }

  @Override
  public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
    BlockEntity blockEntity = level.getBlockEntity(pos);

    if (blockEntity instanceof ATMBlockEntity) {
      drop(level, pos, ((ATMBlockEntity) blockEntity).inventory);
    }
  }

  public void drop(LevelAccessor level, BlockPos pos, ItemStackHandler inventory) {
    if (level instanceof Level realLevel) {
      SimpleContainer inv = new SimpleContainer(inventory.getSlots());
      for (int i = 0; i < inventory.getSlots(); i++) {
        inv.setItem(i, inventory.getStackInSlot(i));
      }
      Containers.dropContents(realLevel, pos, inv);
    }
  }
}
