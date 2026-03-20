package com.denmoth.mothlib;

import com.denmoth.mothlib.config.MothConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MothLibSmokeTest {

    @Test
    void modIdStable() {
        assertEquals("mothlib", MothLib.MODID);
    }

    @Test
    void configDefaultsMatchRegistryKeys() {
        assertTrue(MothConfig.CONFIG_ID_OVERWORLD.startsWith(MothLib.MODID + ":"));
        assertTrue(MothConfig.CONFIG_ID_NETHER.startsWith(MothLib.MODID + ":"));
        assertEquals(32, MothConfig.DEFAULT_OVERWORLD_SPACING);
        assertEquals(8, MothConfig.DEFAULT_OVERWORLD_SEPARATION);
    }
}
