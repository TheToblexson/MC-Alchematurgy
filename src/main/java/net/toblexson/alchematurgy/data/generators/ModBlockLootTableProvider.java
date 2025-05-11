package net.toblexson.alchematurgy.data.generators;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.toblexson.alchematurgy.registry.ModBlocks;

import java.util.Set;

/**
 * Alchematurgy's implementation of the BlockLootSubProvider
 */
public class ModBlockLootTableProvider extends BlockLootSubProvider
{
    /**
     * Create the loot table provider.
     * @param provider the lookup provider.
     */
    public ModBlockLootTableProvider(HolderLookup.Provider provider)
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    /**
     * Generate loot tables for all the blocks
     */
    @Override
    public void generate()
    {
        dropSelf(ModBlocks.ALCHEMICAL_CRUCIBLE.get());
        dropSelf(ModBlocks.ALCHEMICAL_SEPARATOR.get());
        dropSelf(ModBlocks.ALCHEMICAL_PURIFIER.get());
        dropSelf(ModBlocks.ALCHEMICAL_CONCENTRATOR.get());
        dropSelf(ModBlocks.ALCHEMICAL_FABRICATOR.get());
    }

    /**
     * Get all blocks that this loot table handles.
     * @return An iterable containing all the blocks.
     */
    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return ModBlocks.REGISTER.getEntries().stream().map(Holder::value)::iterator;
    }
}
