package net.toblexson.alchematurgy.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
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

/**
 * The Alchemical Crucible block class
 */
public class AlchemicalCrucibleBlock extends ModMenuBlock
{
    public static final MapCodec<AlchemicalCrucibleBlock> CODEC = simpleCodec(AlchemicalCrucibleBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

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
        if (stack.is(Items.WATER_BUCKET) && level.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity blockEntity)
        {
            if (blockEntity.tryAddWater())
            {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, blockHit);
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
     * Create a new block entity.
     * @param pos The block position.
     * @param state the block state.
     * @return The new block entity.
     */
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AlchemicalCrucibleBlockEntity(pos, state);
    }
}
