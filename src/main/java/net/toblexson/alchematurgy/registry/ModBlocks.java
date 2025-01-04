package net.toblexson.alchematurgy.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.function.Supplier;

public class ModBlocks
{
    /**
     * The Deferred Register for all the mod blocks.
     */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Alchematurgy.MOD_ID);

    public static final DeferredBlock<Block> TEST_BLOCK = registerBlock("test_block", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));

    /** Register a block with a block item.
     * @param name The name of the block.
     * @param block The block supplier.
     * @return The generated deferred block.
     * @param <T> The block type.
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {
        DeferredBlock<T> deferredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, deferredBlock);
        return deferredBlock;
    }

    /** Register a block item for a block.
     * @param name The name of the block item.
     * @param block The parent block.
     * @param <T> The block type.
     */
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /** Register all the blocks in the deferred register.
     * @param bus The Event Bus.
     */
    public static void register(IEventBus bus)
    {
        BLOCKS.register(bus);
    }
}