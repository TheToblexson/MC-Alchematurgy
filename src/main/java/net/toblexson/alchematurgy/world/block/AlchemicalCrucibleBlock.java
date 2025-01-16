package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;
import org.jetbrains.annotations.Nullable;

/**
 * The Alchemical Crucible block class
 */
public class AlchemicalCrucibleBlock extends BaseEntityBlock
{
    /**
    * Create the Alchemical Crucible block.
    * @param properties The block properties.
    */
    public AlchemicalCrucibleBlock(Properties properties)
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
        Containers.dropContentsOnDestroy(state, newState, level, pos);
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
            serverPlayer.openMenu(state.getMenuProvider(level,pos));
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
        return new SimpleMenuProvider((containerId, playerInventory, player) ->
                                              new AlchemicalCrucibleMenu(containerId, playerInventory),
                                        Component.translatable("block.alchematurgy.alchemical_crucible"));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType)
    {
        return super.getTicker(pLevel, pState, pBlockEntityType);
    }

    /**
     * I don't understand CODECs yet, so this is just simple and hopefully works.
     * @return The block's CODEC.
     */
    @Override
    public MapCodec<? extends BaseEntityBlock> codec()
    {
        return simpleCodec(AlchemicalCrucibleBlock::new);
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
