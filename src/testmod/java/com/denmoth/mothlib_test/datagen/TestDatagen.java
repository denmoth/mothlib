package com.denmoth.mothlib_test.datagen;

import com.denmoth.mothlib_test.MothLibTest;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class TestDatagen {

    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();

        // WorldGen
        RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(Registries.PROCESSOR_LIST, TestWorldGen::bootstrapProcessors)
                .add(Registries.TEMPLATE_POOL, TestWorldGen::bootstrapPools)
                .add(Registries.STRUCTURE, TestWorldGen::bootstrapStructures)
                .add(Registries.STRUCTURE_SET, TestWorldGen::bootstrapSets);

        event.getGenerator().addProvider(event.includeServer(),
                new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), builder, Set.of(MothLibTest.MODID)));

        // Loot Tables
        event.getGenerator().addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(TestChestLoot::new, LootContextParamSets.CHEST)
        )));
    }
}