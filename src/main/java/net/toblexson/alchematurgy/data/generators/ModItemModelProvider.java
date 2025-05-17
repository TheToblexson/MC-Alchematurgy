package net.toblexson.alchematurgy.data.generators;

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
     * @param output The pack result.
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

        basicItem(ModItems.BOTTLED_MIXED_ESSENCE.get());

        basicItem(ModItems.BOTTLED_DIRTY_AIR_ESSENCE.get());
        basicItem(ModItems.BOTTLED_DIRTY_EARTH_ESSENCE.get());
        basicItem(ModItems.BOTTLED_DIRTY_FIRE_ESSENCE.get());
        basicItem(ModItems.BOTTLED_DIRTY_WATER_ESSENCE.get());
        basicItem(ModItems.BOTTLED_DIRTY_LIFE_ESSENCE.get());
        basicItem(ModItems.BOTTLED_DIRTY_MAGIC_ESSENCE.get());

        basicItem(ModItems.BOTTLED_AIR_ESSENCE.get());
        basicItem(ModItems.BOTTLED_EARTH_ESSENCE.get());
        basicItem(ModItems.BOTTLED_FIRE_ESSENCE.get());
        basicItem(ModItems.BOTTLED_WATER_ESSENCE.get());
        basicItem(ModItems.BOTTLED_LIFE_ESSENCE.get());
        basicItem(ModItems.BOTTLED_MAGIC_ESSENCE.get());

        basicItem(ModItems.BOTTLED_CONCENTRATED_AIR_ESSENCE.get());
        basicItem(ModItems.BOTTLED_CONCENTRATED_EARTH_ESSENCE.get());
        basicItem(ModItems.BOTTLED_CONCENTRATED_FIRE_ESSENCE.get());
        basicItem(ModItems.BOTTLED_CONCENTRATED_WATER_ESSENCE.get());
        basicItem(ModItems.BOTTLED_CONCENTRATED_LIFE_ESSENCE.get());
        basicItem(ModItems.BOTTLED_CONCENTRATED_MAGIC_ESSENCE.get());
    }
}
