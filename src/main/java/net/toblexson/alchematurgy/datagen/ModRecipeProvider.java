package net.toblexson.alchematurgy.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModItems;

import java.util.concurrent.CompletableFuture;

/**
 * Alchematurgy's implementation of the RecipeProvider
 */
public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder
{
    /**
     * Creates the recipe provider
     * @param output The pack output.
     * @param provider The lookup provider.
     */
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider)
    {
        super(output, provider);
    }


    /**
     * Generate the recipes.
     */
    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        //Wand
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WAND.get())
                .pattern("  i")
                .pattern(" s ")
                .pattern("n  ")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('s', Items.STICK)
                .define('n', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output);

        //Alchemical Crucible
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ALCHEMICAL_CRUCIBLE.get())
                .pattern("i i")
                .pattern("iii")
                .pattern("ici")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', Blocks.CAMPFIRE)
                .unlockedBy("has_wand", has(ModItems.WAND))
                .save(output);

            //Alchemical Purifier
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ALCHEMICAL_DISTILLER.get())
                    .pattern("iii")
                    .pattern("igi")
                    .pattern("iii")
                    .define('i', Tags.Items.INGOTS_IRON)
                    .define('g', Tags.Items.INGOTS_GOLD)
                    .unlockedBy("has_crucible", has(ModBlocks.ALCHEMICAL_CRUCIBLE))
                    .save(output);
    }
}
