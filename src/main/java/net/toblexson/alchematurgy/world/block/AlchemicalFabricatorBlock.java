package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalFabricatorBlockEntity;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFabricatorBlock extends ModMenuBlock
{
    public static final MapCodec<AlchemicalFabricatorBlock> CODEC = simpleCodec(AlchemicalFabricatorBlock::new);

    public AlchemicalFabricatorBlock(BlockBehaviour.Properties properties)
    {
        super(properties);
    }

    /**
     * Get the ticker from the block entity.
     * @param level The level that the block exists in.
     * @param state The block state of the block.
     * @param blockEntityType The type of block entity associated with the block.
     * @return The block entity ticker.
     * @param <T> A block entity class.
     */
    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return !level.isClientSide() ? createTickerHelper(blockEntityType, ModBlockEntityTypes.ALCHEMICAL_FABRICATOR.get(),
                                                          (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1)) : null;
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
     * Create a new block entity.
     * @param pos The block position.
     * @param state the block state.
     * @return The new block entity.
     */
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AlchemicalFabricatorBlockEntity(pos, state);
    }
}