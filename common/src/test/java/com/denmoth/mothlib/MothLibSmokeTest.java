package com.denmoth.mothlib;

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
        assertTrue(MothConfigDefaults.CONFIG_ID_OVERWORLD.startsWith(MothLib.MODID + ":"));
        assertTrue(MothConfigDefaults.CONFIG_ID_NETHER.startsWith(MothLib.MODID + ":"));
        assertEquals(32, MothConfigDefaults.DEFAULT_OVERWORLD_SPACING);
        assertEquals(8, MothConfigDefaults.DEFAULT_OVERWORLD_SEPARATION);
    }
}
