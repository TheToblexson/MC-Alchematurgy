package net.toblexson.alchematurgy.world.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MixedEssenceFluid extends Fluid
{
    /**
     * Get the bucket form of the fluid.
     * @return The bucket item for the fluid.
     */
    @Override
    public Item getBucket()
    {
        return null;
    }

    /**
     * Check to see if the fluid can be replaced with the given fluid.
     * @param fluidState The replacing fluid state.
     * @param level The level as a block getter.
     * @param pos The world position.
     * @param fluid The replacing fluid?
     * @param direction unsure?
     * @return TRUE if this fluid can be replaced with the given fluid. FALSE if it cannot.
     */
    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction)
    {
        return false;
    }

    /**
     * @param pBlockReader
     * @param pPos
     * @param pFluidState
     * @return
     */
    @Override
    protected Vec3 getFlow(BlockGetter pBlockReader, BlockPos pPos, FluidState pFluidState)
    {
        return null;
    }

    /**
     * @param pLevel
     * @return
     */
    @Override
    public int getTickDelay(LevelReader pLevel)
    {
        return 0;
    }

    /**
     * @return
     */
    @Override
    protected float getExplosionResistance()
    {
        return 0;
    }

    /**
     * @param pState
     * @param pLevel
     * @param pPos
     * @return
     */
    @Override
    public float getHeight(FluidState pState, BlockGetter pLevel, BlockPos pPos)
    {
        return 0;
    }

    /**
     * @param pState
     * @return
     */
    @Override
    public float getOwnHeight(FluidState pState)
    {
        return 0;
    }

    /**
     * @param pState
     * @return
     */
    @Override
    protected BlockState createLegacyBlock(FluidState pState)
    {
        return null;
    }

    /**
     * @param pState
     * @return
     */
    @Override
    public boolean isSource(FluidState pState)
    {
        return false;
    }

    /**
     * @param pState
     * @return
     */
    @Override
    public int getAmount(FluidState pState)
    {
        return 0;
    }

    /**
     * @param pState
     * @param pLevel
     * @param pPos
     * @return
     */
    @Override
    public VoxelShape getShape(FluidState pState, BlockGetter pLevel, BlockPos pPos)
    {
        return null;
    }
}
