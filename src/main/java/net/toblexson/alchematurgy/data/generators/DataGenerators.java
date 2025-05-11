package net.toblexson.alchematurgy.data.generators;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Creates all of Alchematurgy's data generators
 */
@EventBusSubscriber(modid = Alchematurgy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    /**
     * Adds the data generators to the Gather Data event.
     * @param event The GatherData Event.
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        // Loot Tables
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), provider));

        // Recipes
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, provider));

        // Tags
        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, provider, fileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, provider, blockTagsProvider.contentsGetter(), fileHelper));

        // Block States and Models
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, fileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, fileHelper));
    }
}
