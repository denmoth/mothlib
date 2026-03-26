package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class MothProcessorLists {
    public static final ResourceKey<StructureProcessorList> TERRAIN_MATCH = create("terrain_match");

    private static ResourceKey<StructureProcessorList> create(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation(MothLib.MODID, name));
    }
}
