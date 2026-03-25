package com.denmoth.mothlib.api.datagen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MothLootBuilder {

    private final String path;
    private final BiConsumer<ResourceLocation, LootTable.Builder> writer;
    private final LootTable.Builder table = LootTable.lootTable();

    private MothLootBuilder(String path, BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        this.path = path;
        this.writer = writer;
    }

    public static MothLootBuilder create(String path, BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        return new MothLootBuilder(path, writer);
    }

    public MothLootBuilder pool(int rollsMin, int rollsMax, Consumer<PoolBuilder> consumer) {
        PoolBuilder pb = new PoolBuilder();
        consumer.accept(pb);
        pb.builder.setRolls(UniformGenerator.between(rollsMin, rollsMax));
        table.withPool(pb.builder);
        return this;
    }

    public void build(String modId) {
        writer.accept(new ResourceLocation(modId, path), table);
    }

    public void build(ResourceLocation id) {
        writer.accept(id, table);
    }

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

        /**
         * Adds an item with a custom NBT tag.
         */
        public PoolBuilder addWithNbt(Item item, int weight, int min, int max, CompoundTag tag) {
            builder.add(LootItem.lootTableItem(item)
                    .setWeight(weight)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(SetNbtFunction.setTag(tag)));
            return this;
        }

        /**
         * Adds a written book with custom randomized text.
         */
        public PoolBuilder addBook(int weight, String title, String author, String... pagesJson) {
            CompoundTag tag = new CompoundTag();
            tag.putString("title", title);
            tag.putString("author", author);
            ListTag pages = new ListTag();
            for (String page : pagesJson) {
                // page should be a JSON string like "{\"text\":\"Hello World\"}"
                pages.add(StringTag.valueOf(page));
            }
            tag.put("pages", pages);
            
            builder.add(LootItem.lootTableItem(Items.WRITTEN_BOOK)
                    .setWeight(weight)
                    .apply(SetNbtFunction.setTag(tag)));
            return this;
        }

        /**
         * Adds armor meant for an armor stand.
         * The armor stand equipment order in loot tables generally populates based on roll order or specific slots.
         * For 1.20, entity equipment from loot tables drops into their inventory.
         * Armor stands auto-equip armor if they have empty slots.
         */
        public PoolBuilder addArmorSet(int weight, Item head, Item chest, Item legs, Item feet) {
            if (head != null) add(head, weight);
            if (chest != null) add(chest, weight);
            if (legs != null) add(legs, weight);
            if (feet != null) add(feet, weight);
            return this;
        }
    }
}
