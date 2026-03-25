package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.tags.MothBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.tags.BiomeTags;

import java.util.concurrent.CompletableFuture;

/**
 * Cross-loader biome tags with optional entries for popular biome mods (IDs may vary by version; missing biomes are ignored).
 */
public class MothBiomeTagsProvider extends TagsProvider<Biome> {

    public MothBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        treeTags();
        soilTags();
        climateAndTerrainTags();
        miscTags();
    }

    private void treeTags() {
        tag(MothBiomeTags.TREES_OAK)
                .add(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.SWAMP, Biomes.PLAINS, Biomes.WINDSWEPT_FOREST, Biomes.RIVER)
                .addOptional(rl("biomesoplenty", "seasonal_forest"))
                .addOptional(rl("biomesoplenty", "woodland"))
                .addOptional(rl("biomesoplenty", "orchard"))
                .addOptional(rl("biomesoplenty", "grassland"))
                .addOptional(rl("biomesoplenty", "field"))
                .addOptional(rl("terralith", "temperate_highlands"))
                .addOptional(rl("terralith", "forested_highlands"))
                .addOptional(rl("terralith", "moonlight_grove"))
                .addOptional(rl("terralith", "shield_clearing"))
                .addOptional(rl("biomeswevegone", "aspen_borealis"))
                .addOptional(rl("biomeswevegone", "amaranth_grassland"))
                .addOptional(rl("regions_unexplored", "clover_patch"))
                .addOptional(rl("regions_unexplored", "prairie"));

        tag(MothBiomeTags.TREES_BIRCH)
                .add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST)
                .addOptional(rl("biomesoplenty", "birch_grove"))
                .addOptional(rl("biomesoplenty", "snowy_coniferous_forest"))
                .addOptional(rl("terralith", "birch_taiga"))
                .addOptional(rl("terralith", "lavender_forest"))
                .addOptional(rl("terralith", "lavender_valley"))
                .addOptional(rl("biomeswevegone", "aspen_borealis"))
                .addOptional(rl("regions_unexplored", "boreal_taiga"));

        tag(MothBiomeTags.TREES_SPRUCE)
                .add(Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.SNOWY_TAIGA, Biomes.GROVE)
                .addOptional(rl("biomesoplenty", "coniferous_forest"))
                .addOptional(rl("biomesoplenty", "fir_clearing"))
                .addOptional(rl("biomesoplenty", "snowy_fir_clearing"))
                .addOptional(rl("biomesoplenty", "snowy_coniferous_forest"))
                .addOptional(rl("terralith", "winter_taiga"))
                .addOptional(rl("terralith", "cold_shrubland"))
                .addOptional(rl("terralith", "alpine_grove"))
                .addOptional(rl("biomeswevegone", "coniferous_forest"))
                .addOptional(rl("regions_unexplored", "blackwood_taiga"))
                .addOptional(rl("regions_unexplored", "cold_boreal_taiga"));

        tag(MothBiomeTags.TREES_JUNGLE)
                .add(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE)
                .addOptional(rl("biomesoplenty", "rainforest"))
                .addOptional(rl("biomesoplenty", "rocky_rainforest"))
                .addOptional(rl("biomesoplenty", "fungal_jungle"))
                .addOptional(rl("terralith", "rocky_jungle"))
                .addOptional(rl("terralith", "jungle_mountains"))
                .addOptional(rl("biomeswevegone", "cypress_swamplands"))
                .addOptional(rl("regions_unexplored", "bamboo_forest"));

        tag(MothBiomeTags.TREES_DARK_OAK)
                .add(Biomes.DARK_FOREST)
                .addOptional(rl("biomesoplenty", "ominous_woods"))
                .addOptional(rl("biomesoplenty", "dead_forest"))
                .addOptional(rl("terralith", "ominous_meadow"))
                .addOptional(rl("regions_unexplored", "blackwood_taiga"));

        tag(MothBiomeTags.TREES_ACACIA)
                .add(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA)
                .addOptional(rl("biomesoplenty", "scrubland"))
                .addOptional(rl("biomesoplenty", "shrubland"))
                .addOptional(rl("terralith", "ashen_savanna"))
                .addOptional(rl("terralith", "windswept_savanna"))
                .addOptional(rl("biomeswevegone", "baobab_savanna"))
                .addOptional(rl("biomeswevegone", "atacama_outback"))
                .addOptional(rl("regions_unexplored", "arid_mountains"));

        tag(MothBiomeTags.TREES_MANGROVE)
                .add(Biomes.MANGROVE_SWAMP)
                .addOptional(rl("biomeswevegone", "white_mangrove_marshes"))
                .addOptional(rl("biomeswevegone", "cypress_swamplands"))
                .addOptional(rl("regions_unexplored", "bayou"));

        tag(MothBiomeTags.TREES_CHERRY)
                .add(Biomes.CHERRY_GROVE)
                .addOptional(rl("terralith", "sakura_grove"))
                .addOptional(rl("biomeswevegone", "cherry_blossom_grove"))
                .addOptional(rl("regions_unexplored", "alpha_grove"));

        tag(MothBiomeTags.TREES_MAPLE)
                .addOptional(rl("biomesoplenty", "maple_woods"))
                .addOptional(rl("biomesoplenty", "snowy_maple_woods"))
                .addOptional(rl("autumnity", "maple_forest"))
                .addOptional(rl("autumnity", "maple_hills"))
                .addOptional(rl("regions_unexplored", "maple_forest"));

        tag(MothBiomeTags.TREES_REDWOOD)
                .addOptional(rl("biomesoplenty", "redwood_forest"))
                .addOptional(rl("biomeswevegone", "redwood_thicket"))
                .addOptional(rl("terralith", "red_oasis"));
    }

    private void soilTags() {
        tag(MothBiomeTags.SOIL_GRASS)
                .add(Biomes.PLAINS, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.SUNFLOWER_PLAINS)
                .addOptional(rl("biomesoplenty", "grassland"))
                .addOptional(rl("terralith", "blooming_valley"))
                .addOptional(rl("regions_unexplored", "clover_patch"));

        tag(MothBiomeTags.SOIL_PODZOL)
                .add(Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.GROVE)
                .addOptional(rl("biomesoplenty", "coniferous_forest"))
                .addOptional(rl("terralith", "cloud_forest"));

        tag(MothBiomeTags.SOIL_SAND)
                .add(Biomes.DESERT, Biomes.BEACH)
                .addOptional(rl("biomesoplenty", "lush_desert"))
                .addOptional(rl("terralith", "ancient_sands"))
                .addOptional(rl("terralith", "desert_oasis"))
                .addOptional(rl("biomeswevegone", "sierra_badlands"))
                .addOptional(rl("regions_unexplored", "chalk_cliffs"));

        tag(MothBiomeTags.SOIL_SNOW)
                .add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH, Biomes.ICE_SPIKES, Biomes.FROZEN_RIVER)
                .addOptional(rl("biomesoplenty", "muskeg"))
                .addOptional(rl("terralith", "glacial_shelf"))
                .addOptional(rl("regions_unexplored", "frozen_pine_taiga"));

        tag(MothBiomeTags.SOIL_MUD)
                .add(Biomes.MANGROVE_SWAMP, Biomes.SWAMP)
                .addOptional(rl("biomesoplenty", "marsh"))
                .addOptional(rl("biomesoplenty", "wetland"))
                .addOptional(rl("biomeswevegone", "cypress_swamplands"))
                .addOptional(rl("regions_unexplored", "bayou"));

        tag(MothBiomeTags.SOIL_MUSHROOM)
                .add(Biomes.MUSHROOM_FIELDS)
                .addOptional(rl("biomesoplenty", "fungal_jungle"))
                .addOptional(rl("terralith", "moonlight_grove"));
    }

    private void climateAndTerrainTags() {
        tag(MothBiomeTags.CLIMATE_COLD)
                .add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH, Biomes.ICE_SPIKES, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN)
                .addOptional(rl("biomesoplenty", "muskeg"))
                .addOptional(rl("terralith", "frozen_cliffs"))
                .addOptional(rl("regions_unexplored", "cold_boreal_taiga"));

        tag(MothBiomeTags.CLIMATE_HOT)
                .add(Biomes.DESERT, Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY)
                .addOptional(rl("biomesoplenty", "volcano"))
                .addOptional(rl("terralith", "tropical_volcano"))
                .addOptional(rl("biomeswevegone", "atacama_outback"));

        tag(MothBiomeTags.CLIMATE_TEMPERATE)
                .add(Biomes.PLAINS, Biomes.FOREST, Biomes.RIVER, Biomes.OCEAN)
                .addOptional(rl("terralith", "temperate_highlands"))
                .addOptional(rl("regions_unexplored", "prairie"));

        tag(MothBiomeTags.TERRAIN_MOUNTAIN)
                .add(Biomes.JAGGED_PEAKS, Biomes.FROZEN_PEAKS, Biomes.STONY_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS)
                .addOptional(rl("terralith", "rocky_mountains"))
                .addOptional(rl("terralith", "painted_mountains"))
                .addOptional(rl("biomeswevegone", "sierra_badlands"))
                .addOptional(rl("regions_unexplored", "arid_mountains"));

        tag(MothBiomeTags.TERRAIN_WETLAND)
                .add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP)
                .addOptional(rl("biomesoplenty", "marsh"))
                .addOptional(rl("biomesoplenty", "wetland"))
                .addOptional(rl("terralith", "ice_marsh"))
                .addOptional(rl("regions_unexplored", "bayou"));

        tag(MothBiomeTags.TERRAIN_COASTAL)
                .add(Biomes.BEACH, Biomes.STONY_SHORE, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN)
                .addOptional(rl("terralith", "gravel_beach"))
                .addOptional(rl("biomeswevegone", "white_mangrove_marshes"));
    }

    private void miscTags() {
        tag(MothBiomeTags.WITHOUT_STRUCTURES)
                .addOptionalTag(BiomeTags.IS_OCEAN.location())
                .addOptionalTag(BiomeTags.IS_RIVER.location())
                .add(Biomes.THE_VOID);
    }

    private static ResourceLocation rl(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
}
