package net.toblexson.alchematurgy.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.block.*;

import java.util.function.Supplier;

/**
 * Alchematurgy's Block registration.
 */
public class ModBlocks
{
    /**
     * The Deferred Register for all the mod blocks.
     */
    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(Alchematurgy.MOD_ID);

    public static final DeferredBlock<Block> ALCHEMICAL_CRUCIBLE = registerBlock("alchemical_crucible", () ->
            new AlchemicalCrucibleBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ALCHEMICAL_SEPARATOR = registerBlock("alchemical_separator", () ->
            new AlchemicalSeparatorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ALCHEMICAL_PURIFIER = registerBlock("alchemical_purifier", () ->
            new AlchemicalPurifierBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ALCHEMICAL_CONCENTRATOR = registerBlock("alchemical_concentrator", () ->
            new AlchemicalConcentratorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ALCHEMICAL_FABRICATOR = registerBlock("alchemical_fabricator", () ->
            new AlchemicalFabricatorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.METAL)));

//    public static final DeferredBlock<Block> FUNNEL = registerBlock("funnel", () ->
//            new FunnelBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.METAL)));

    /**
     * Register a block with a block item.
     * @param name The name of the block.
     * @param block The block supplier.
     * @return The generated deferred block.
     * @param <T> The block type.
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {

        DeferredBlock<T> deferredBlock = REGISTER.register(name, block);
        registerBlockItem(name, deferredBlock);
        return deferredBlock;
    }

    /**
     * Register a block item for a block.
     * @param name The name of the block item.
     * @param block The parent block.
     * @param <T> The block type.
     */
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        ModItems.REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * Register all the blocks in the deferred register.
     * @param bus The Event Bus.
     */
    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
