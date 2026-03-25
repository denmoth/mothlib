package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.MothLib;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class MothDataGen {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new MothWorldGenProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new MothBiomeTagsProvider(packOutput, lookupProvider));
    }
}