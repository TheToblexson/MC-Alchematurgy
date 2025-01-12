package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;
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
