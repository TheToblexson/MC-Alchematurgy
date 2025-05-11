package net.toblexson.alchematurgy.data.generators;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.concurrent.CompletableFuture;

/**
 * Alchematurgy's implementation of the BlockTagsProvider.
 */
public class ModBlockTagsProvider extends BlockTagsProvider
{
    /**
     * Create the block tags provider.
     * @param output The pack result.
     * @param provider The lookup provider.
     * @param fileHelper The existing file helper.
     */
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper fileHelper)
    {
        super(output, provider, Alchematurgy.MOD_ID, fileHelper);
    }

    /**
     * Add tags to the provider.
     * @param provider The lookup provider.
     */
    @Override
    protected void addTags(HolderLookup.Provider provider)
    {

    }
}
