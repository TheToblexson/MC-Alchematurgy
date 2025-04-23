package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The Alchemical Crucible block class
 */
public class AlchemicalCrucibleBlock extends BaseEntityBlock
{
    public static final MapCodec<AlchemicalCrucibleBlock> CODEC = simpleCodec(AlchemicalCrucibleBlock::new);

    /**
    * Create the Alchemical Crucible block.
    * @param properties The block properties.
    */
    public AlchemicalCrucibleBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
    {
        super.tick(pState, pLevel, pPos, pRandom);
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
