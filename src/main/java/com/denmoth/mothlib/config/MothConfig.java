package com.denmoth.mothlib.config;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.api.MothConfigRegistry;
import net.minecraftforge.common.ForgeConfigSpec;

public class MothConfig {

    /** Default spacing/separation (single source for spec + generated datapack snapshots). */
    public static final int DEFAULT_OVERWORLD_SPACING = 32;
    public static final int DEFAULT_OVERWORLD_SEPARATION = 8;
    public static final int DEFAULT_NETHER_SPACING = 24;
    public static final int DEFAULT_NETHER_SEPARATION = 6;

    /** Keys for {@link com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement#configId}. */
    public static final String CONFIG_ID_OVERWORLD = MothLib.MODID + ":overworld";
    public static final String CONFIG_ID_NETHER = MothLib.MODID + ":nether";

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> OVERWORLD_SPACING;
    public static final ForgeConfigSpec.ConfigValue<Integer> OVERWORLD_SEPARATION;

    public static final ForgeConfigSpec.ConfigValue<Integer> NETHER_SPACING;
    public static final ForgeConfigSpec.ConfigValue<Integer> NETHER_SEPARATION;

    static {
        BUILDER.push("Global Structures");

        BUILDER.push("Overworld Defaults");
        OVERWORLD_SPACING = BUILDER.define("spacing", DEFAULT_OVERWORLD_SPACING);
        OVERWORLD_SEPARATION = BUILDER.define("separation", DEFAULT_OVERWORLD_SEPARATION);
        BUILDER.pop();

        BUILDER.push("Nether Defaults");
        NETHER_SPACING = BUILDER.define("spacing", DEFAULT_NETHER_SPACING);
        NETHER_SEPARATION = BUILDER.define("separation", DEFAULT_NETHER_SEPARATION);
        BUILDER.pop();

        BUILDER.pop();
        COMMON_SPEC = BUILDER.build();
    }

    public static void registerConfigKeys() {
        MothConfigRegistry.register(CONFIG_ID_OVERWORLD, OVERWORLD_SPACING::get, OVERWORLD_SEPARATION::get);
        MothConfigRegistry.register(CONFIG_ID_NETHER, NETHER_SPACING::get, NETHER_SEPARATION::get);
    }
}