package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalSeparatorBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AlchemicalSeparatorBlock  extends BaseEntityBlock
{
    public static final MapCodec<AlchemicalSeparatorBlock> CODEC = simpleCodec(AlchemicalSeparatorBlock::new);

    public AlchemicalSeparatorBlock(Properties properties)
    {
        super(properties);
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
            if (level.getBlockEntity(pos) instanceof AlchemicalSeparatorBlockEntity blockEntity)
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
     * @param blockHit The block hit result.
     * @return The interaction result.
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHit)
    {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer)
            serverPlayer.openMenu(Objects.requireNonNull(state.getMenuProvider(level, pos)), pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * React to the player right-clicking whilst holding an item stack.
     * @param stack The held item stack.
     * @param state The block state.
     * @param level The game level.
     * @param pos The block position.
     * @param player The player.
     * @param hand The hand holding the item stack.
     * @param blockHit The block hit result.
     * @return The interaction result.
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHit)
    {
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
        if (level.getBlockEntity(pos) instanceof AlchemicalSeparatorBlockEntity blockEntity)
            return new SimpleMenuProvider(blockEntity, getName());
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
            return createTickerHelper(blockEntityType, ModBlockEntityTypes.ALCHEMICAL_SEPARATOR.get(),
                                      (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
        return null;
    }

    /**
     * Returns the block's CODEC
     * @return The CODEC
     */
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
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
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AlchemicalSeparatorBlockEntity(pos, state);
    }
}
