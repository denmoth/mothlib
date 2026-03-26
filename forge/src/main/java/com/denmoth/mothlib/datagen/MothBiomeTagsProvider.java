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
                .addOptional(rl("terralith", "winter_taiga"));

        tag(MothBiomeTags.TREES_ACACIA)
                .addOptionalTag(BiomeTags.IS_SAVANNA.location())
                .addOptional(rl("terralith", "windswept_savanna"));

        tag(MothBiomeTags.TREES_CHERRY)
                .add(Biomes.CHERRY_GROVE)
                .addOptional(rl("terralith", "sakura_grove"))
                .addOptional(rl("regions_unexplored", "alpha_grove"))
                .addOptional(rl("biomeswevegone", "cherry_blossom_grove"));

        tag(MothBiomeTags.TREES_DARK_OAK)
                .add(Biomes.DARK_FOREST)
                .addOptional(rl("biomesoplenty", "ominous_woods"))
                .addOptional(rl("regions_unexplored", "blackwood_taiga"));

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
                .add(Biomes.DESERT)
                .addOptional(rl("biomesoplenty", "wasteland"))
                .addOptional(rl("terralith", "desert_oasis"));

        tag(MothBiomeTags.SOIL_SNOW)
                .add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES)
                .addOptional(rl("biomesoplenty", "cold_desert"))
                .addOptional(rl("terralith", "glacial_shelf"));
    }

    private static ResourceLocation rl(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
}
