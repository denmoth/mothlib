package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MothPlacements {
    public static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, MothLib.MODID);

    public static final RegistryObject<StructurePlacementType<ConfigurableStructurePlacement>> CONFIGURABLE =
            PLACEMENT_TYPES.register("configurable", () -> () -> ConfigurableStructurePlacement.CODEC);

    public static void register(IEventBus eventBus) {
        PLACEMENT_TYPES.register(eventBus);
    }
}