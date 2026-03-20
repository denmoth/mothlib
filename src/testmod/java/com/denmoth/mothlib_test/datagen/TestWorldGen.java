package com.denmoth.mothlib_test.datagen;

import com.denmoth.mothlib.api.worldgen.MothPoolBuilder;
import com.denmoth.mothlib.api.worldgen.MothProcessorBuilder;
import com.denmoth.mothlib.api.worldgen.MothStructureBuilder;
import com.denmoth.mothlib.api.worldgen.MothStructureSetBuilder;
import com.denmoth.mothlib.config.MothConfig;
import com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement;
import com.denmoth.mothlib_test.MothLibTest;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.Optional;

public class TestWorldGen {

    /** Fixed salt so test structure set placement stays reproducible across runs. */
    private static final int MAIN_STRUCTURE_SET_SALT = 12_345_678;

    public static final ResourceKey<StructureProcessorList> PROC_MOSSIFY = createKey(Registries.PROCESSOR_LIST, "mossify");
    public static final ResourceKey<StructureTemplatePool> POOL_START = createKey(Registries.TEMPLATE_POOL, "test_structure/start");
    public static final ResourceKey<Structure> STRUCT_MAIN = createKey(Registries.STRUCTURE, "main_structure");
    public static final ResourceKey<StructureSet> SET_MAIN = createKey(Registries.STRUCTURE_SET, "main_structures");

    public static void bootstrapProcessors(BootstapContext<StructureProcessorList> ctx) {
        MothProcessorBuilder.create(ctx)
                .replace(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE, 0.5f)
                .replace(Blocks.DIRT, Blocks.DIAMOND_BLOCK, 0.1f)
                .register(PROC_MOSSIFY);
    }

    public static void bootstrapPools(BootstapContext<StructureTemplatePool> ctx) {
        MothPoolBuilder.create(ctx, MothLibTest.MODID)
                .add("test_structure/base", 1)
                .add("test_structure/base", 2, ctx.lookup(Registries.PROCESSOR_LIST).getOrThrow(PROC_MOSSIFY))
                .register(POOL_START);
    }

    public static void bootstrapStructures(BootstapContext<Structure> ctx) {
        MothStructureBuilder.create(ctx)
                .startPool(POOL_START)
                .surface()
                .depth(6)
                .register(STRUCT_MAIN);
    }

    public static void bootstrapSets(BootstapContext<StructureSet> ctx) {
        // Spacing/separation match MothConfig defaults so generated JSON matches runtime when config is default.
        MothStructureSetBuilder.create(ctx)
                .add(STRUCT_MAIN, 1)
                .placement(new ConfigurableStructurePlacement(
                        Vec3i.ZERO,
                        StructurePlacement.FrequencyReductionMethod.DEFAULT,
                        1.0f,
                        MAIN_STRUCTURE_SET_SALT,
                        Optional.empty(),
                        MothConfig.DEFAULT_OVERWORLD_SPACING,
                        MothConfig.DEFAULT_OVERWORLD_SEPARATION,
                        RandomSpreadType.LINEAR,
                        MothConfig.CONFIG_ID_OVERWORLD
                ))
                .register(SET_MAIN);
    }

    private static <T> ResourceKey<T> createKey(ResourceKey<net.minecraft.core.Registry<T>> registry, String name) {
        return ResourceKey.create(registry, new ResourceLocation(MothLibTest.MODID, name));
    }
}