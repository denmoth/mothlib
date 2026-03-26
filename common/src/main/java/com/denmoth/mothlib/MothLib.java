package com.denmoth.mothlib;

import com.denmoth.mothlib.registry.MothPlacements;

public final class MothLib {
    public static final String MODID = "mothlib";

    public static void init() {
        com.denmoth.mothlib.registry.MothBlocks.register();
        com.denmoth.mothlib.registry.MothStructureProcessorTypes.register();
        MothPlacements.register();
        com.denmoth.mothlib.registry.MothStructureTypes.register();
    }

    private MothLib() {
    }
}
