package com.denmoth.mothlib.api.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.tags.BiomeTags;

import java.util.Map;
import java.util.Optional;

public class MothStructureBuilder {
    private final BootstapContext<Structure> context;
    private ResourceKey<StructureTemplatePool> startPool;
    private int maxDepth = 6;
    private HeightProvider startHeight = ConstantHeight.of(VerticalAnchor.absolute(0));
    private Heightmap.Types projectStartToHeightmap = Heightmap.Types.WORLD_SURFACE_WG;
    private TerrainAdjustment terrainAdaptation = TerrainAdjustment.BEARD_THIN;
    private TagKey<Biome> biomes = BiomeTags.IS_OVERWORLD;
    private int maxDistanceFromCenter = 80;

    private MothStructureBuilder(BootstapContext<Structure> context) {
        this.context = context;
    }

    public static MothStructureBuilder create(BootstapContext<Structure> context) {
        return new MothStructureBuilder(context);
    }

    public MothStructureBuilder startPool(ResourceKey<StructureTemplatePool> pool) { this.startPool = pool; return this; }
    public MothStructureBuilder depth(int depth) { this.maxDepth = depth; return this; }
    public MothStructureBuilder maxDistanceFromCenter(int blocks) { this.maxDistanceFromCenter = blocks; return this; }
    public MothStructureBuilder startHeight(HeightProvider height) { this.startHeight = height; return this; }
    public MothStructureBuilder biomes(TagKey<Biome> tag) { this.biomes = tag; return this; }
    public MothStructureBuilder surface() { this.projectStartToHeightmap = Heightmap.Types.WORLD_SURFACE_WG; return this; }
    public MothStructureBuilder ocean() { this.projectStartToHeightmap = Heightmap.Types.OCEAN_FLOOR_WG; return this; }
    public MothStructureBuilder underground() { this.terrainAdaptation = TerrainAdjustment.BURY; return this; }

    public void register(ResourceKey<Structure> key) {
        if (startPool == null) throw new IllegalStateException("Start pool is required for " + key.location());

        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);

        Structure.StructureSettings settings = new Structure.StructureSettings(
                biomeGetter.getOrThrow(biomes),
                Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                terrainAdaptation
        );

        context.register(key, new JigsawStructure(
                settings,
                poolGetter.getOrThrow(startPool),
                Optional.empty(),
                maxDepth,
                startHeight,
                false,
                Optional.ofNullable(projectStartToHeightmap),
                maxDistanceFromCenter
        ));
    }

    public void registerNether(ResourceKey<Structure> key, int scanStartY) {
        if (startPool == null) throw new IllegalStateException("Start pool is required for " + key.location());

        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);

        Structure.StructureSettings settings = new Structure.StructureSettings(
                biomeGetter.getOrThrow(biomes),
                Map.of(),
                GenerationStep.Decoration.UNDERGROUND_DECORATION, // Nether structures usually generate underground
                terrainAdaptation
        );

        context.register(key, new com.denmoth.mothlib.worldgen.structure.NetherJigsawStructure(
                settings,
                poolGetter.getOrThrow(startPool),
                Optional.empty(),
                maxDepth,
                startHeight,
                Optional.ofNullable(projectStartToHeightmap),
                maxDistanceFromCenter,
                scanStartY
        ));
    }
}