package net.toblexson.alchematurgy.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.registry.ModItems;

/**
 * Alchematurgy's implementation of the ItemModelProvider
 */
public class ModItemModelProvider extends ItemModelProvider
{
    /**
     * Create the item model provider
     * @param output The pack output.
     * @param fileHelper The existing file helper
     */
    public ModItemModelProvider(PackOutput output, ExistingFileHelper fileHelper)
    {
        super(output, Alchematurgy.MOD_ID, fileHelper);
    }

    /**
     * Create/register the item model jsons.
     */
    @Override
    protected void registerModels()
    {
        basicItem(ModItems.WAND.get());
        basicItem(ModItems.ASH.get());

        basicItem(ModItems.ALCHEMICAL_ASH.get());

        basicItem(ModItems.ELEMENT_EARTH.get());
        basicItem(ModItems.ELEMENT_WATER.get());
        basicItem(ModItems.ELEMENT_AIR.get());
        basicItem(ModItems.ELEMENT_FIRE.get());
    }
}
