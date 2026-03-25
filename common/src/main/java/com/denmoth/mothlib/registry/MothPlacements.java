package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public final class MothPlacements {
    public static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_TYPES =
            DeferredRegister.create(MothLib.MODID, Registries.STRUCTURE_PLACEMENT);

    public static final RegistrySupplier<StructurePlacementType<ConfigurableStructurePlacement>> CONFIGURABLE =
            PLACEMENT_TYPES.register("configurable", () -> () -> ConfigurableStructurePlacement.CODEC);

    private MothPlacements() {
    }

    public static void register() {
        PLACEMENT_TYPES.register();
    }
}
