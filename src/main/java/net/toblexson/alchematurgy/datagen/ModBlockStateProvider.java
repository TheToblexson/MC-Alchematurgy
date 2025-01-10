package net.toblexson.alchematurgy.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.registry.ModBlocks;

/**
 * Alchematurgy's implementation of the BlockStateProvider
 */
public class ModBlockStateProvider extends BlockStateProvider
{
    /**
     * Create the block state provider
     * @param output The pack output.
     * @param fileHelper The existing file helper
     */
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper fileHelper)
    {
        super(output, Alchematurgy.MOD_ID, fileHelper);
    }

    /**
     * Create/register the block state and model jsons.
     */
    @Override
    protected void registerStatesAndModels()
    {
        block(ModBlocks.ALCHEMICAL_CRUCIBLE);
        block(ModBlocks.ALCHEMICAL_DISTILLER);
    }

    /**
     * Generate files for a simple cube block with a standard block item from a deferred block.
     * @param deferredBlock The deferred block.
     */
    private void block(DeferredBlock<?> deferredBlock)
    {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
