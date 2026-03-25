package com.denmoth.mothlib;

/**
 * Loader-agnostic defaults and config key ids (Forge spec uses these; Fabric registers the same keys with constant suppliers until a Fabric config exists).
 */
public final class MothConfigDefaults {

    public static final int DEFAULT_OVERWORLD_SPACING = 32;
    public static final int DEFAULT_OVERWORLD_SEPARATION = 8;
    public static final int DEFAULT_NETHER_SPACING = 24;
    public static final int DEFAULT_NETHER_SEPARATION = 6;

    public static final String CONFIG_ID_OVERWORLD = MothLib.MODID + ":overworld";
    public static final String CONFIG_ID_NETHER = MothLib.MODID + ":nether";

    private MothConfigDefaults() {
    }
}
