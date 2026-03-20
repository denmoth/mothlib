package com.denmoth.mothlib.api.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.ArrayList;
import java.util.List;

public class MothStructureSetBuilder {
    private final BootstapContext<StructureSet> context;
    private final List<StructureSet.StructureSelectionEntry> entries = new ArrayList<>();
    private StructurePlacement placement;

    private MothStructureSetBuilder(BootstapContext<StructureSet> context) {
        this.context = context;
    }

    public static MothStructureSetBuilder create(BootstapContext<StructureSet> context) {
        return new MothStructureSetBuilder(context);
    }

    public MothStructureSetBuilder add(ResourceKey<Structure> structure, int weight) {
        Holder<Structure> holder = context.lookup(Registries.STRUCTURE).getOrThrow(structure);
        entries.add(StructureSet.entry(holder, weight));
        return this;
    }

    public MothStructureSetBuilder placement(StructurePlacement placement) {
        this.placement = placement;
        return this;
    }

    public void register(ResourceKey<StructureSet> key) {
        if (placement == null) throw new IllegalStateException("Placement not set!");
        context.register(key, new StructureSet(List.copyOf(entries), placement));
    }
}