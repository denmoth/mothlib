package com.denmoth.mothlib.api.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MothPoolBuilder {
    private final BootstapContext<StructureTemplatePool> context;
    private final String modId;
    private final List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> elements = new ArrayList<>();
    private Holder<StructureTemplatePool> fallback;
    private StructureTemplatePool.Projection projection = StructureTemplatePool.Projection.RIGID;

    private MothPoolBuilder(BootstapContext<StructureTemplatePool> context, String modId) {
        this.context = context;
        this.modId = modId;
        this.fallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);
    }

    public static MothPoolBuilder create(BootstapContext<StructureTemplatePool> context, String modId) {
        return new MothPoolBuilder(context, modId);
    }

    public MothPoolBuilder terminator(ResourceKey<StructureTemplatePool> fallbackKey) {
        this.fallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(fallbackKey);
        return this;
    }

    public MothPoolBuilder projection(StructureTemplatePool.Projection projection) {
        this.projection = projection;
        return this;
    }

    public MothPoolBuilder add(String path, int weight) {
        return add(path, weight, getEmptyProcessors());
    }

    public MothPoolBuilder add(String path, int weight, Holder<StructureProcessorList> processors) {
        return add(new ResourceLocation(modId, path), weight, processors);
    }

    public MothPoolBuilder add(ResourceLocation location, int weight) {
        return add(location, weight, getEmptyProcessors());
    }

    public MothPoolBuilder add(ResourceLocation location, int weight, Holder<StructureProcessorList> processors) {
        Function<StructureTemplatePool.Projection, ? extends StructurePoolElement> element =
                StructurePoolElement.single(location.toString(), processors);
        elements.add(Pair.of(element, weight));
        return this;
    }

    public MothPoolBuilder addEmpty(int weight) {
        elements.add(Pair.of(StructurePoolElement.empty(), weight));
        return this;
    }

    public void register(ResourceKey<StructureTemplatePool> key) {
        context.register(key, new StructureTemplatePool(fallback, List.copyOf(elements), projection));
    }

    // FIXED METHOD
    private Holder<StructureProcessorList> getEmptyProcessors() {
        ResourceKey<StructureProcessorList> emptyKey = ResourceKey.create(
                Registries.PROCESSOR_LIST,
                new ResourceLocation("minecraft", "empty")
        );
        return context.lookup(Registries.PROCESSOR_LIST).getOrThrow(emptyKey);
    }
}