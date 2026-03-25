package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.platform.MothServices;
import com.denmoth.mothlib.platform.services.IMothDeferredRegister;
import com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.function.Supplier;

public final class MothPlacements {
    public static final IMothDeferredRegister<StructurePlacementType<?>> PLACEMENT_TYPES =
            MothServices.REGISTRY.create(MothLib.MODID, Registries.STRUCTURE_PLACEMENT);

    public static final Supplier<StructurePlacementType<ConfigurableStructurePlacement>> CONFIGURABLE =
            PLACEMENT_TYPES.register("configurable", () -> () -> ConfigurableStructurePlacement.CODEC);

    private MothPlacements() {
    }

    public static void register() {
        PLACEMENT_TYPES.register();
    }
}
