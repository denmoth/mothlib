package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.MothLib;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MothLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MothDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 1. Генерация JSON-ов для мира (Placements, Features, Biomes)
        generator.addProvider(event.includeServer(), new MothWorldGenProvider(packOutput, lookupProvider));

        // 2. Сюда позже добавим генерацию моделей, тегов и лут-таблиц
    }
}