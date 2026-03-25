package com.denmoth.mothlib.fabric;

import com.denmoth.mothlib.MothLib;
import net.fabricmc.api.ModInitializer;

public class MothLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MothLib.init();
        MothFabricConfigBootstrap.register();
    }
}
