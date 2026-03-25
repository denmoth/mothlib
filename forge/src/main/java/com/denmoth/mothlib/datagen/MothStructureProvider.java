package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.Optional;

public abstract class MothStructureProvider {

    protected BootstapContext<StructureSet> context;
    protected HolderGetter<Structure> structures;

    public void run(BootstapContext<StructureSet> context) {
        this.context = context;
        this.structures = context.lookup(Registries.STRUCTURE);
        registerSets();
    }

    protected abstract void registerSets();

    /**
     * Registers a {@link StructureSet} with {@link ConfigurableStructurePlacement} tied to
     * {@link com.denmoth.mothlib.api.MothConfigRegistry}.
     */
    protected void register(ResourceKey<StructureSet> key, ResourceKey<Structure> structureKey, String configId, int defaultSpacing, int defaultSeparation, int salt) {
        context.register(key, new StructureSet(
                structures.getOrThrow(structureKey),
                new ConfigurableStructurePlacement(
                        Vec3i.ZERO,
                        StructurePlacement.FrequencyReductionMethod.DEFAULT,
                        1.0f,
                        salt,
                        Optional.empty(),
                        defaultSpacing,
                        defaultSeparation,
                        RandomSpreadType.LINEAR,
                        configId
                )
        ));
    }
}