package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.platform.MothServices;
import com.denmoth.mothlib.platform.services.IMothDeferredRegister;
import com.denmoth.mothlib.worldgen.structure.NetherJigsawStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public class MothStructureTypes {
    public static final IMothDeferredRegister<StructureType<?>> STRUCTURE_TYPES = MothServices.REGISTRY.create(MothLib.MODID, Registries.STRUCTURE_TYPE);

    public static final Supplier<StructureType<NetherJigsawStructure>> NETHER_JIGSAW = STRUCTURE_TYPES.register("nether_jigsaw", () -> () -> NetherJigsawStructure.CODEC);

    public static void register() {
        STRUCTURE_TYPES.register();
    }
}
