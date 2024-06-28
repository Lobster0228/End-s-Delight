package cn.foggyhillside.ends_delight.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
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
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Iterator;

public class EDPieBlock extends Block {
    public static final DirectionProperty FACING;
    public static final IntProperty BITES;
    protected static final VoxelShape SHAPE;
    public final Item pieSlice;

    public EDPieBlock(AbstractBlock.Settings properties, Item pieSlice) {
        super(properties);
        this.pieSlice = pieSlice;
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(BITES, 0));
    }

    public ItemStack getPieSliceItem() {
        return new ItemStack((ItemConvertible)this.pieSlice);
    }

    public int getMaxBites() {
        return 4;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        return (BlockState)this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing());
    }

    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getStackInHand(hand);
        if (level.isClient) {
            if (heldStack.isIn(ModTags.KNIVES)) {
                return this.cutSlice(level, pos, state, player);
            }

            if (this.consumeBite(level, pos, state, player) == ActionResult.SUCCESS) {
                return ActionResult.SUCCESS;
            }

            if (heldStack.isEmpty()) {
                return ActionResult.CONSUME;
            }
        }

        return heldStack.isIn(ModTags.KNIVES) ? this.cutSlice(level, pos, state, player) : this.consumeBite(level, pos, state, player);
    }

    protected ActionResult consumeBite(World level, BlockPos pos, BlockState state, PlayerEntity playerIn) {
        if (!playerIn.canConsume(false)) {
            return ActionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            FoodComponent sliceFood = sliceStack.getItem().getFoodComponent();
            playerIn.getHungerManager().eat(sliceStack.getItem(), sliceStack);
            if (this.getPieSliceItem().getItem().isFood() && sliceFood != null) {
                Iterator var7 = sliceFood.getStatusEffects().iterator();

                while(var7.hasNext()) {
                    Pair<StatusEffectInstance, Float> pair = (Pair)var7.next();
                    if (!level.isClient && pair.getFirst() != null && level.random.nextFloat() < (Float)pair.getSecond()) {
                        playerIn.addStatusEffect(new StatusEffectInstance((StatusEffectInstance)pair.getFirst()));
                    }
                }
            }

            int bites = (Integer)state.get(BITES);
            if (bites < this.getMaxBites() - 1) {
                level.setBlockState(pos, (BlockState)state.with(BITES, bites + 1), 3);
            } else {
                level.removeBlock(pos, false);
            }

            level.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);
            return ActionResult.SUCCESS;
        }
    }

    protected ActionResult cutSlice(World level, BlockPos pos, BlockState state, PlayerEntity player) {
        int bites = (Integer)state.get(BITES);
        if (bites < this.getMaxBites() - 1) {
            level.setBlockState(pos, (BlockState)state.with(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getHorizontalFacing().getOpposite();
        ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getOffsetX() * 0.15, 0.05, (double)direction.getOffsetZ() * 0.15);
        level.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
        return ActionResult.SUCCESS;
    }

    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canPlaceAt(level, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
        return level.getBlockState(pos.down()).isSolid();
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BITES});
    }

    public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
        return this.getMaxBites() - (Integer)blockState.get(BITES);
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public boolean canPathfindThrough(BlockState state, BlockView level, BlockPos pos, NavigationType type) {
        return false;
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
        BITES = IntProperty.of("bites", 0, 3);
        SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    }
}
