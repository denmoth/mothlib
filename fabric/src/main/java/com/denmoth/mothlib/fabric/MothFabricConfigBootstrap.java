package com.denmoth.mothlib.fabric;

import com.denmoth.mothlib.MothConfigDefaults;
import com.denmoth.mothlib.api.MothConfigRegistry;

/**
 * Fabric: wire structure spacing keys to defaults until a dedicated Fabric config exists.
 */
public final class MothFabricConfigBootstrap {

    private MothFabricConfigBootstrap() {
    }

    public static void register() {
        MothConfigRegistry.register(
                MothConfigDefaults.CONFIG_ID_OVERWORLD,
                () -> MothConfigDefaults.DEFAULT_OVERWORLD_SPACING,
                () -> MothConfigDefaults.DEFAULT_OVERWORLD_SEPARATION
        );
        MothConfigRegistry.register(
                MothConfigDefaults.CONFIG_ID_NETHER,
                () -> MothConfigDefaults.DEFAULT_NETHER_SPACING,
                () -> MothConfigDefaults.DEFAULT_NETHER_SEPARATION
        );
    }
}
