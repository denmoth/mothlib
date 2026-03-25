package com.denmoth.mothlib.config;

import com.denmoth.mothlib.MothConfigDefaults;
import com.denmoth.mothlib.api.MothConfigRegistry;
import net.minecraftforge.common.ForgeConfigSpec;

public class MothConfig {

    /** @deprecated Use {@link MothConfigDefaults} from common when referencing defaults without Forge. */
    @Deprecated
    public static final int DEFAULT_OVERWORLD_SPACING = MothConfigDefaults.DEFAULT_OVERWORLD_SPACING;
    @Deprecated
    public static final int DEFAULT_OVERWORLD_SEPARATION = MothConfigDefaults.DEFAULT_OVERWORLD_SEPARATION;
    @Deprecated
    public static final int DEFAULT_NETHER_SPACING = MothConfigDefaults.DEFAULT_NETHER_SPACING;
    @Deprecated
    public static final int DEFAULT_NETHER_SEPARATION = MothConfigDefaults.DEFAULT_NETHER_SEPARATION;

    /** Keys for {@link com.denmoth.mothlib.worldgen.placement.ConfigurableStructurePlacement#configId}. */
    @Deprecated
    public static final String CONFIG_ID_OVERWORLD = MothConfigDefaults.CONFIG_ID_OVERWORLD;
    @Deprecated
    public static final String CONFIG_ID_NETHER = MothConfigDefaults.CONFIG_ID_NETHER;

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> OVERWORLD_SPACING;
    public static final ForgeConfigSpec.ConfigValue<Integer> OVERWORLD_SEPARATION;

    public static final ForgeConfigSpec.ConfigValue<Integer> NETHER_SPACING;
    public static final ForgeConfigSpec.ConfigValue<Integer> NETHER_SEPARATION;

    static {
        BUILDER.push("Global Structures");

        BUILDER.push("Overworld Defaults");
        OVERWORLD_SPACING = BUILDER.define("spacing", MothConfigDefaults.DEFAULT_OVERWORLD_SPACING);
        OVERWORLD_SEPARATION = BUILDER.define("separation", MothConfigDefaults.DEFAULT_OVERWORLD_SEPARATION);
        BUILDER.pop();

        BUILDER.push("Nether Defaults");
        NETHER_SPACING = BUILDER.define("spacing", MothConfigDefaults.DEFAULT_NETHER_SPACING);
        NETHER_SEPARATION = BUILDER.define("separation", MothConfigDefaults.DEFAULT_NETHER_SEPARATION);
        BUILDER.pop();

        BUILDER.pop();
        COMMON_SPEC = BUILDER.build();
    }

    public static void registerConfigKeys() {
        MothConfigRegistry.register(MothConfigDefaults.CONFIG_ID_OVERWORLD, OVERWORLD_SPACING::get, OVERWORLD_SEPARATION::get);
        MothConfigRegistry.register(MothConfigDefaults.CONFIG_ID_NETHER, NETHER_SPACING::get, NETHER_SEPARATION::get);
    }
}