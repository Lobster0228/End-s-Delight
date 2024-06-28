package cn.foggyhillside.ends_delight.block;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.utility.TextUtils;


public class EDFeastBlock extends Block {

        public static final DirectionProperty FACING;
        public static final IntProperty SERVINGS;
        public final Item servingItem;
        public final boolean hasLeftovers;
        protected static final VoxelShape[] SHAPES;

        public EDFeastBlock(AbstractBlock.Settings properties, Item servingItem, boolean hasLeftovers) {
            super(properties);
            this.servingItem = servingItem;
            this.hasLeftovers = hasLeftovers;
            this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(this.getServingsProperty(), this.getMaxServings()));
        }

        public IntProperty getServingsProperty() {
            return SERVINGS;
        }

        public int getMaxServings() {
            return 4;
        }

        public ItemStack getServingItem(BlockState state) {
            return new ItemStack((ItemConvertible)this.servingItem);
        }

        public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
            return SHAPES[(Integer)state.get(SERVINGS)];
        }

        public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
            return level.isClient && this.takeServing(level, pos, state, player, hand).isAccepted() ? ActionResult.SUCCESS : this.takeServing(level, pos, state, player, hand);
        }

        protected ActionResult takeServing(WorldAccess level, BlockPos pos, BlockState state, PlayerEntity player, Hand hand) {
            int servings = (Integer)state.get(this.getServingsProperty());
            if (servings == 0) {
                level.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
                level.breakBlock(pos, true);
                return ActionResult.SUCCESS;
            } else {
                ItemStack serving = this.getServingItem(state);
                ItemStack heldStack = player.getStackInHand(hand);
                if (servings > 0) {
                    if (serving.getRecipeRemainder().isEmpty() || ItemStack.areItemsEqual(heldStack, serving.getRecipeRemainder())) {
                        level.setBlockState(pos, (BlockState)state.with(this.getServingsProperty(), servings - 1), 3);
                        if (!player.getAbilities().creativeMode && !serving.getRecipeRemainder().isEmpty()) {
                            heldStack.decrement(1);
                        }

                        if (!player.getInventory().insertStack(serving)) {
                            player.dropItem(serving, false);
                        }

                        if ((Integer)level.getBlockState(pos).get(this.getServingsProperty()) == 0 && !this.hasLeftovers) {
                            level.removeBlock(pos, false);
                        }

                        level.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return ActionResult.SUCCESS;
                    }

                    player.sendMessage(TextUtils.getTranslation("block.feast.use_container", new Object[]{serving.getRecipeRemainder().getName()}), true);
                }

                return ActionResult.PASS;
            }
        }

        public BlockState getPlacementState(ItemPlacementContext context) {
            return (BlockState)this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
        }

        public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
            return facing == Direction.DOWN && !stateIn.canPlaceAt(level, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
        }

        public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
            return level.getBlockState(pos.down()).isSolid();
        }

        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            builder.add(new Property[]{FACING, SERVINGS});
        }

        public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
            return (Integer)blockState.get(this.getServingsProperty());
        }

        public boolean hasComparatorOutput(BlockState state) {
            return true;
        }

        public boolean canPathfindThrough(BlockState state, BlockView level, BlockPos pos, NavigationType type) {
            return false;
        }

        static {
            FACING = Properties.HORIZONTAL_FACING;
            SERVINGS = IntProperty.of("servings", 0, 4);
            SHAPES = new VoxelShape[]{Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 1.0, 14.0), Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 6.0, 14.0), Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 8.0, 14.0), Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 10.0, 14.0)};
        }
    }

