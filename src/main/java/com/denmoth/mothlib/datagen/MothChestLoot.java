package com.denmoth.mothlib.datagen;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class MothChestLoot implements LootTableSubProvider {

    // Вспомогательный класс для пулов
    public static class PoolBuilder {
        private final LootPool.Builder builder = LootPool.lootPool();

        public PoolBuilder add(Item item, int weight) {
            builder.add(LootItem.lootTableItem(item).setWeight(weight));
            return this;
        }

        public PoolBuilder add(Item item, int weight, int min, int max) {
            builder.add(LootItem.lootTableItem(item)
                    .setWeight(weight)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))));
            return this;
        }

        // Метод для добавления редких предметов (шанс < 100% при ролле) может быть добавлен здесь
    }

    protected MothChestBuilder addChest(String path, BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        return new MothChestBuilder(path, writer);
    }

    public class MothChestBuilder {
        private final String path;
        private final BiConsumer<ResourceLocation, LootTable.Builder> writer;
        private final LootTable.Builder table = LootTable.lootTable();

        public MothChestBuilder(String path, BiConsumer<ResourceLocation, LootTable.Builder> writer) {
            this.path = path;
            this.writer = writer;
        }

        public MothChestBuilder pool(int rollsMin, int rollsMax, Consumer<PoolBuilder> consumer) {
            PoolBuilder pb = new PoolBuilder();
            consumer.accept(pb);
            pb.builder.setRolls(UniformGenerator.between(rollsMin, rollsMax));
            table.withPool(pb.builder);
            return this;
        }

        public void build(String modId) {
            writer.accept(new ResourceLocation(modId, path), table);
        }
    }
}