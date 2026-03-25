package com.denmoth.mothlib.forge;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.config.MothConfig;
import com.denmoth.mothlib.datagen.MothDataGen;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MothLib.MODID)
public class MothLibForge {

    public MothLibForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(MothLib.MODID, modBus);
        modBus.addListener(MothLibForge::onGatherData);

        MothLib.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MothConfig.COMMON_SPEC);
        MothConfig.registerConfigKeys();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void onGatherData(GatherDataEvent event) {
        MothDataGen.gatherData(event);
    }
}
