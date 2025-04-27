package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The Alchemical Crucible block class
 */
public class AlchemicalCrucibleBlock extends BaseEntityBlock
{
    public static final MapCodec<AlchemicalCrucibleBlock> CODEC = simpleCodec(AlchemicalCrucibleBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    /**
    * Create the Alchemical Crucible block.
    * @param properties The block properties.
    */
    public AlchemicalCrucibleBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false));
    }

    /**
     * Apply the block properties to the builder.
     * @param builder The state definition builder.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    /**
     * Drop contents and remove the block entity when removed.
     * @param state The block state.
     * @param level The game level.
     * @param pos The block pos.
     * @param newState The block state replacing the block.
     * @param movedByPiston Was the block moved by a piston?
     */
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
    {
        if (state.getBlock() != newState.getBlock())
            if (level.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity blockEntity)
            {
                blockEntity.dropInventory();
                level.updateNeighbourForOutputSignal(pos,this);
            }
        super.onRemove(state,level,pos,newState,movedByPiston);
    }

    /**
     * React to the player right-clicking whilst not holding an item
     * @param state The block state.
     * @param level The game level.
     * @param pos The block position.
     * @param player The player.
     * @param hitResult The hit result.
     * @return The interaction result.
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer)
            serverPlayer.openMenu(Objects.requireNonNull(state.getMenuProvider(level, pos)), pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if (stack.is(Items.WATER_BUCKET) && level.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity blockEntity)
        {
            if (blockEntity.tryAddWater())
            {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer)
            serverPlayer.openMenu(Objects.requireNonNull(state.getMenuProvider(level, pos)), pos);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Get this block's menu provider.
     * @param state The block state.
     * @param level The game level.
     * @param pos The block position.
     * @return The menu provider.
     */
    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity blockEntity)
            return new SimpleMenuProvider(blockEntity, this.getName());
        return null;
    }

    /**
     * Get the ticker from the block entity.
     * @param level The level that the block exists in.
     * @param state The block state of the block.
     * @param blockEntityType The type of block entity associated with the block.
     * @return The block entity ticker.
     * @param <T> A block entity class.
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        if (!level.isClientSide())
            return createTickerHelper(blockEntityType, ModBlockEntityTypes.ALCHEMICAL_CRUCIBLE.get(),
                                      (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
        return null;
    }

    /**
     * Returns the block's CODEC
     * @return The CODEC
     */
    @Override
    public MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }

    /**
     * Get the block's render shape.
     * @param state The block state.
     * @return The render shape.
     */
    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    /**
     * Create a new block entity.
     * @param pos The block position.
     * @param state the block state.
     * @return The new block entity.
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AlchemicalCrucibleBlockEntity(pos, state);
    }
}
