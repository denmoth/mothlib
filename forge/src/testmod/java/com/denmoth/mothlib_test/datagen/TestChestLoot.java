package com.denmoth.mothlib_test.datagen;

import com.denmoth.mothlib.datagen.MothChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class TestChestLoot extends MothChestLoot {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        addChest("chests/test_loot", writer)
                .pool(1, 3, p -> p
                        .add(Items.DIAMOND, 5)
                        .add(Items.DIRT, 20, 1, 64)
                )
                .build("mothlib_test");
    }
}