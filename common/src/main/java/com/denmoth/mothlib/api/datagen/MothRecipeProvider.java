package com.denmoth.mothlib.api.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public abstract class MothRecipeProvider extends RecipeProvider {

    public MothRecipeProvider(PackOutput output) {
        super(output);
    }

    /**
     * Helper for a shaped 3x3 recipe.
     * Example: shaped(writer, RecipeCategory.MISC, Items.DIAMOND_SWORD, " D ", " D ", " S ", 'D', Items.DIAMOND, 'S', Items.STICK);
     */
    protected void shaped(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike result, String r1, String r2, String r3, Object... keys) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(cat, result)
                .pattern(r1)
                .pattern(r2)
                .pattern(r3);

        for (int i = 0; i < keys.length; i += 2) {
            Character key = (Character) keys[i];
            Object item = keys[i + 1];
            if (item instanceof ItemLike) {
                builder.define(key, (ItemLike) item);
            } else if (item instanceof Ingredient) {
                builder.define(key, (Ingredient) item);
            }
        }

        builder.unlockedBy(getHasName(result), has(result))
                .save(writer);
    }

    /**
     * Turns 9 items into 1 block (e.g. 9 Iron Ingots -> 1 Iron Block)
     */
    protected void compact(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike block, ItemLike ingot) {
        ShapedRecipeBuilder.shaped(cat, block)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ingot)
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(writer);
    }

    /**
     * Turns 1 block into 9 items (e.g. 1 Iron Block -> 9 Iron Ingots)
     */
    protected void uncompact(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike ingot, ItemLike block) {
        ShapelessRecipeBuilder.shapeless(cat, ingot, 9)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(writer);
    }
    
    /**
     * Turns 4 items into 1 block (e.g. 4 Sand -> 1 Sandstone)
     */
    protected void compact2x2(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike block, ItemLike item) {
        ShapedRecipeBuilder.shaped(cat, block)
                .pattern("##")
                .pattern("##")
                .define('#', item)
                .unlockedBy(getHasName(item), has(item))
                .save(writer);
    }

    /**
     * Smelting helper
     */
    protected void smelting(Consumer<FinishedRecipe> writer, ItemLike input, ItemLike output, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, xp, time)
                .unlockedBy(getHasName(input), has(input))
                .save(writer);
    }

    /**
     * Blasting helper
     */
    protected void blasting(Consumer<FinishedRecipe> writer, ItemLike input, ItemLike output, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, xp, time)
                .unlockedBy(getHasName(input), has(input))
                .save(writer, getConversionRecipeName(output, input) + "_from_blasting");
    }
}
