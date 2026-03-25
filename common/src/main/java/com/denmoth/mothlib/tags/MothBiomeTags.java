package com.denmoth.mothlib.tags;

import com.denmoth.mothlib.MothLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Cross-mod biome tags (optional entries for BOP, Terralith, OTBWG, Regions Unexplored, Autumnity).
 */
public final class MothBiomeTags {

    // --- Tree dominance / presence (grouped by vanilla wood family) ---
    public static final TagKey<Biome> TREES_OAK = create("trees/oak");
    public static final TagKey<Biome> TREES_BIRCH = create("trees/birch");
    public static final TagKey<Biome> TREES_SPRUCE = create("trees/spruce");
    public static final TagKey<Biome> TREES_JUNGLE = create("trees/jungle");
    public static final TagKey<Biome> TREES_DARK_OAK = create("trees/dark_oak");
    public static final TagKey<Biome> TREES_ACACIA = create("trees/acacia");
    public static final TagKey<Biome> TREES_MANGROVE = create("trees/mangrove");
    public static final TagKey<Biome> TREES_CHERRY = create("trees/cherry");
    public static final TagKey<Biome> TREES_MAPLE = create("trees/maple");
    public static final TagKey<Biome> TREES_REDWOOD = create("trees/redwood");

    // --- Surface / soil character (approximate, for worldgen logic) ---
    public static final TagKey<Biome> SOIL_GRASS = create("soil/grass");
    public static final TagKey<Biome> SOIL_PODZOL = create("soil/podzol");
    public static final TagKey<Biome> SOIL_SAND = create("soil/sand");
    public static final TagKey<Biome> SOIL_SNOW = create("soil/snow");
    public static final TagKey<Biome> SOIL_MUD = create("soil/mud");
    public static final TagKey<Biome> SOIL_MUSHROOM = create("soil/mushroom");

    // --- Climate / terrain buckets ---
    public static final TagKey<Biome> CLIMATE_COLD = create("climate/cold");
    public static final TagKey<Biome> CLIMATE_HOT = create("climate/hot");
    public static final TagKey<Biome> CLIMATE_TEMPERATE = create("climate/temperate");
    public static final TagKey<Biome> TERRAIN_MOUNTAIN = create("terrain/mountain");
    public static final TagKey<Biome> TERRAIN_WETLAND = create("terrain/wetland");
    public static final TagKey<Biome> TERRAIN_COASTAL = create("terrain/coastal");

    // --- Misc ---
    public static final TagKey<Biome> WITHOUT_STRUCTURES = create("without_structures");

    private MothBiomeTags() {
    }

    private static TagKey<Biome> create(String path) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(MothLib.MODID, path));
    }
}
