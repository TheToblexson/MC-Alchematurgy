package net.toblexson.alchematurgy.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.concurrent.CompletableFuture;

/**
 * Alchematurgy's implementation of the ItemTagsProvider.
 */
public class ModItemTagsProvider extends ItemTagsProvider
{
    /**
     * Create the block tags provider.
     * @param output     The pack output.
     * @param provider   The lookup provider.
     * @param blockTags  The block tags.
     * @param fileHelper The existing file helper.
     */
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper fileHelper)
    {
        super(output, provider, blockTags, Alchematurgy.MOD_ID, fileHelper);
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
