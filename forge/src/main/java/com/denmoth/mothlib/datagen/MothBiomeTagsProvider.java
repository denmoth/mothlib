package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.tags.MothBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class MothBiomeTagsProvider extends TagsProvider<Biome> {

    public MothBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        treeTags();
        landscapeTags();
        climateTags();
    }

    private void treeTags() {
        tag(MothBiomeTags.TREES_OAK)
                .add(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.PLAINS)
                .addOptionalTag(rl("forge", "is_deciduous"))
                .addOptionalTag(rl("c", "tree_deciduous"))
                .addOptional(rl("biomesoplenty", "orchard"))
                .addOptional(rl("biomesoplenty", "woodland"))
                .addOptional(rl("terralith", "moonlight_grove"));

        tag(MothBiomeTags.TREES_BIRCH)
                .add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST)
                .addOptional(rl("terralith", "birch_taiga"))
                .addOptional(rl("biomesoplenty", "birch_grove"))
                .addOptional(rl("regions_unexplored", "boreal_taiga"));

        tag(MothBiomeTags.TREES_SPRUCE)
                .addOptionalTag(BiomeTags.IS_TAIGA.location())
                .addOptionalTag(rl("forge", "is_coniferous"))
                .addOptionalTag(rl("c", "tree_coniferous"))
                .addOptional(rl("terralith", "winter_taiga"))
                .addOptional(rl("nyctophobia", "haunted_forest"));

        tag(MothBiomeTags.TREES_ACACIA)
                .addOptionalTag(BiomeTags.IS_SAVANNA.location())
                .addOptional(rl("terralith", "windswept_savanna"));

        tag(MothBiomeTags.TREES_CHERRY)
                .add(Biomes.CHERRY_GROVE)
                .addOptional(rl("terralith", "sakura_grove"))
                .addOptional(rl("regions_unexplored", "alpha_grove"))
                .addOptional(rl("biomeswevegone", "cherry_blossom_grove"))
                .addOptional(rl("promenade", "blush_sakura_grove"))
                .addOptional(rl("promenade", "cotton_sakura_grove"));

        tag(MothBiomeTags.TREES_DARK_OAK)
                .add(Biomes.DARK_FOREST)
                .addOptional(rl("biomesoplenty", "ominous_woods"))
                .addOptional(rl("regions_unexplored", "blackwood_taiga"))
                .addOptional(rl("promenade", "dark_amaranth_forest"))
                .addOptional(rl("promenade", "tall_dark_amaranth_forest"))
                .addOptional(rl("nyctophobia", "deep_dark_forest"));

        tag(MothBiomeTags.TREES_JUNGLE)
                .addOptionalTag(BiomeTags.IS_JUNGLE.location())
                .addOptional(rl("biomesoplenty", "rainforest"))
                .addOptional(rl("terralith", "rocky_jungle"));

        tag(MothBiomeTags.TREES_MANGROVE)
                .add(Biomes.MANGROVE_SWAMP)
                .addOptional(rl("biomeswevegone", "white_mangrove_marshes"));

        tag(MothBiomeTags.TREES_MAPLE)
                .addOptional(rl("biomesoplenty", "maple_woods"))
                .addOptional(rl("autumnity", "maple_forest"))
                .addOptional(rl("regions_unexplored", "maple_forest"));

        tag(MothBiomeTags.TREES_TALL)
                .add(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST)
                .addOptional(rl("biomesoplenty", "redwood_forest"))
                .addOptional(rl("terralith", "siberian_taiga"))
                .addOptional(rl("terralith", "moonlight_grove"))
                .addOptional(rl("regions_unexplored", "redwood_forest"))
                .addOptional(rl("regions_unexplored", "boreal_taiga"))
                .addOptional(rl("biomeswevegone", "redwood_thicket"))
                .addOptional(rl("promenade", "tall_dark_amaranth_forest"))
                .addOptional(rl("nyctophobia", "deep_dark_forest"))
                .addOptional(rl("nyctophobia", "haunted_forest"));
    }

    private void landscapeTags() {
        tag(MothBiomeTags.SOIL_GRASS)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.MEADOW)
                .addOptional(rl("terralith", "blooming_valley"))
                .addOptional(rl("biomesoplenty", "pasture"))
                .addOptional(rl("regions_unexplored", "prairie"));

        tag(MothBiomeTags.TERRAIN_MOUNTAIN)
                .addOptionalTag(BiomeTags.IS_MOUNTAIN.location())
                .addOptional(rl("terralith", "painted_mountains"))
                .addOptional(rl("regions_unexplored", "arid_mountains"));

        tag(MothBiomeTags.SOIL_SAND)
                .addOptionalTag(BiomeTags.IS_BADLANDS.location())
                .add(Biomes.DESERT, Biomes.BEACH, Biomes.SNOWY_BEACH)
                .addOptional(rl("biomesoplenty", "wasteland"))
                .addOptional(rl("terralith", "desert_oasis"))
                .addOptional(rl("nyctophobia", "eroded_haunted_forest"));

        tag(MothBiomeTags.SOIL_SNOW)
                .add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA)
                .addOptional(rl("biomesoplenty", "cold_desert"))
                .addOptional(rl("terralith", "glacial_shelf"));

        tag(MothBiomeTags.SOIL_PODZOL)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .addOptional(rl("biomesoplenty", "coniferous_forest"));

        tag(MothBiomeTags.SOIL_MUD)
                .add(Biomes.MANGROVE_SWAMP)
                .addOptional(rl("biomesoplenty", "bog"))
                .addOptional(rl("biomeswevegone", "cypress_swamps"))
                .addOptional(rl("nyctophobia", "haunted_lakes"));

        tag(MothBiomeTags.SOIL_MUSHROOM)
                .add(Biomes.MUSHROOM_FIELDS);

        tag(MothBiomeTags.TERRAIN_WETLAND)
                .add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP)
                .addOptional(rl("biomesoplenty", "marsh"))
                .addOptional(rl("biomesoplenty", "bayou"))
                .addOptional(rl("terralith", "orchid_swamp"))
                .addOptional(rl("regions_unexplored", "marsh"))
                .addOptional(rl("nyctophobia", "haunted_lakes"));

        tag(MothBiomeTags.TERRAIN_COASTAL)
                .add(Biomes.BEACH, Biomes.SNOWY_BEACH, Biomes.STONY_SHORE)
                .addOptionalTag(rl("forge", "is_beach"))
                .addOptionalTag(rl("c", "beach"))
                .addOptional(rl("nyctophobia", "ancient_dead_coral_reef"));

        tag(MothBiomeTags.WITHOUT_STRUCTURES)
                .add(Biomes.THE_VOID);
    }

    private void climateTags() {
        tag(MothBiomeTags.CLIMATE_HOT)
                .add(Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA, Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE)
                .addOptionalTag(rl("forge", "is_hot"))
                .addOptionalTag(rl("c", "climate_hot"));

        tag(MothBiomeTags.CLIMATE_COLD)
                .add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH, Biomes.GROVE, Biomes.SNOWY_SLOPES, Biomes.JAGGED_PEAKS, Biomes.FROZEN_PEAKS)
                .addOptionalTag(rl("forge", "is_cold"))
                .addOptionalTag(rl("c", "climate_cold"));

        tag(MothBiomeTags.CLIMATE_TEMPERATE)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.SWAMP, Biomes.TAIGA, Biomes.MEADOW)
                .addOptionalTag(rl("forge", "is_temperate"))
                .addOptionalTag(rl("c", "climate_temperate"))
                .addOptional(rl("nyctophobia", "haunted_forest"))
                .addOptional(rl("nyctophobia", "haunted_lakes"))
                .addOptional(rl("nyctophobia", "eroded_haunted_forest"))
                .addOptional(rl("nyctophobia", "deep_dark_forest"));
    }

    private static ResourceLocation rl(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
}
