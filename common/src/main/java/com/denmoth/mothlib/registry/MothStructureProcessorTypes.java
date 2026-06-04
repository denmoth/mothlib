package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.api.RegHelper;
import com.denmoth.mothlib.worldgen.processor.MothTerrainMatchProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.function.Supplier;

public class MothStructureProcessorTypes {
    public static final RegHelper REGISTRY = new RegHelper(MothLib.MODID);

    public static final Supplier<StructureProcessorType<MothTerrainMatchProcessor>> TERRAIN_MATCH = REGISTRY.structureProcessorType("terrain_match", MothTerrainMatchProcessor.CODEC);
    public static final Supplier<StructureProcessorType<com.denmoth.mothlib.worldgen.processor.RandomWheatProcessor>> RANDOM_WHEAT = REGISTRY.structureProcessorType("random_wheat", com.denmoth.mothlib.worldgen.processor.RandomWheatProcessor.CODEC);
    public static final Supplier<StructureProcessorType<com.denmoth.mothlib.worldgen.processor.WindmillBearingProcessor>> WINDMILL_BEARING = REGISTRY.structureProcessorType("windmill_bearing", com.denmoth.mothlib.worldgen.processor.WindmillBearingProcessor.CODEC);
    public static final Supplier<StructureProcessorType<com.denmoth.mothlib.worldgen.processor.MothTreeMatchProcessor>> TREE_MATCH = REGISTRY.structureProcessorType("tree_match", com.denmoth.mothlib.worldgen.processor.MothTreeMatchProcessor.CODEC);

    public static void register() {
        REGISTRY.register();
    }
}
