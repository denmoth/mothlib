package com.denmoth.mothlib.api.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.function.Function;

public class MothJigsawHelper {

    public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String id, String modId) {
        return StructurePoolElement.single(new ResourceLocation(modId, id).toString());
    }

    public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String id, String modId, Holder<StructureProcessorList> processors) {
        return StructurePoolElement.single(new ResourceLocation(modId, id).toString(), processors);
    }

    public static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> entry(String id, String modId, int weight) {
        return Pair.of(single(id, modId), weight);
    }

    public static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> entry(String id, String modId, int weight, Holder<StructureProcessorList> processors) {
        return Pair.of(single(id, modId, processors), weight);
    }

    // TODO: Add support for LegacySinglePoolElement if needed
}