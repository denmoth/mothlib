package com.denmoth.mothlib.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public abstract class MothRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public MothRecipeProvider(PackOutput output) {
        super(output);
    }

    // === Упрощенные методы ===

    // Крафт 3x3 одной строкой
    // shaped(CONSUMER, RecipeCategory.MISC, REG.MY_ITEM.get(),
    //        " A ",
    //        "ABA",
    //        " A ",
    //        'A', Items.GOLD, 'B', Items.DIAMOND);
    protected void shaped(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike result, String r1, String r2, String r3, Object... keys) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(cat, result)
                .pattern(r1)
                .pattern(r2)
                .pattern(r3);

        for (int i = 0; i < keys.length; i += 2) {
            Character key = (Character) keys[i];
            ItemLike item = (ItemLike) keys[i + 1];
            builder.define(key, item);
        }

        builder.unlockedBy(getHasName(result), has(result))
                .save(writer);
    }

    // Превращение 9 предметов в блок (Ingot -> Block)
    protected void compact(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike block, ItemLike ingot) {
        ShapedRecipeBuilder.shaped(cat, block)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ingot)
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(writer);
    }

    // Обратно (Block -> 9 Ingots)
    protected void uncompact(Consumer<FinishedRecipe> writer, RecipeCategory cat, ItemLike ingot, ItemLike block) {
        ShapelessRecipeBuilder.shapeless(cat, ingot, 9)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(writer);
    }
}