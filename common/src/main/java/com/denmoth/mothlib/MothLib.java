package com.denmoth.mothlib;

import com.denmoth.mothlib.registry.MothPlacements;

public final class MothLib {
    public static final String MODID = "mothlib";

    public static void init() {
        MothPlacements.register();
        com.denmoth.mothlib.registry.MothStructureTypes.register();
    }

    private MothLib() {
    }
}
