package com.denmoth.mothlib;

import com.denmoth.mothlib.config.MothConfig;
import com.denmoth.mothlib.registry.MothPlacements;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MothLib.MODID)
public class MothLib {
    public static final String MODID = "mothlib";

    public MothLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MothPlacements.register(modEventBus);

        // Регистрируем сам конфиг Forge
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MothConfig.COMMON_SPEC);

        // Регистрируем ключи для использования в JSON
        MothConfig.registerConfigKeys();

        MinecraftForge.EVENT_BUS.register(this);
    }
}