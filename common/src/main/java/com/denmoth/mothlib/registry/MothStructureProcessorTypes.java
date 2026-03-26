package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.api.RegHelper;
import com.denmoth.mothlib.worldgen.processor.MothTerrainMatchProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.function.Supplier;

public class MothStructureProcessorTypes {
    public static final RegHelper REGISTRY = new RegHelper(MothLib.MODID);

    public static final Supplier<StructureProcessorType<MothTerrainMatchProcessor>> TERRAIN_MATCH = REGISTRY.structureProcessorType("terrain_match", MothTerrainMatchProcessor.CODEC);

    public static void register() {
        REGISTRY.register();
    }
}
