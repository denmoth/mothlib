package com.denmoth.mothlib.forge;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.config.MothConfig;
import com.denmoth.mothlib.datagen.MothDataGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.CreativeModeTabs;
import com.denmoth.mothlib.registry.MothBlocks;
import net.minecraft.world.item.DyeColor;

@Mod(MothLib.MODID)
public class MothLibForge {

    public MothLibForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(MothLibForge::onGatherData);
        modBus.addListener(this::addCreative);

        MothLib.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MothConfig.COMMON_SPEC);
        MothConfig.registerConfigKeys();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(MothBlocks.GROUND_MARKER.get());
            for (DyeColor color : DyeColor.values()) {
                event.accept(MothBlocks.COLORED_MARKERS.get(color).get());
            }
        }
    }

    public static void onGatherData(GatherDataEvent event) {
        MothDataGen.gatherData(event);
    }
}
