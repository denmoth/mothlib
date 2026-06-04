package com.denmoth.mothlib.fabric;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.registry.MothBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;

public class MothLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MothLib.init();
        MothFabricConfigBootstrap.register();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {
            if (entries.shouldShowOpRestrictedItems()) {
                entries.accept(MothBlocks.GROUND_MARKER.get());
                for (DyeColor color : DyeColor.values()) {
                    entries.accept(MothBlocks.COLORED_MARKERS.get(color).get());
                }
            }
        });
    }
}
