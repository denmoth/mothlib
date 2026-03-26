package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.MothLib;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Этот провайдер генерирует JSON файлы для ConfiguredFeatures, Placements и BiomeModifiers.
 * Мы используем его, чтобы зарегистрировать наши "динамические" объекты.
 */
public class MothWorldGenProvider extends DatapackBuiltinEntriesProvider {

    // Сюда мы будем добавлять билдеры. Сейчас он пустой, но
    // в твоих модах ты будешь добавлять сюда .add(Registries.STRUCTURE, ...)
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PROCESSOR_LIST, context -> {
                context.register(
                        net.minecraft.resources.ResourceKey.create(Registries.PROCESSOR_LIST, new net.minecraft.resources.ResourceLocation(MothLib.MODID, "terrain_match")),
                        new net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList(
                                java.util.List.of(com.denmoth.mothlib.worldgen.processor.MothTerrainMatchProcessor.INSTANCE)
                        )
                );
            });

    public MothWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MothLib.MODID));
    }
}