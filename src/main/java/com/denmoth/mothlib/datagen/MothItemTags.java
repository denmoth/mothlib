package com.denmoth.mothlib.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider; // Исправлен импорт
import net.minecraft.data.tags.TagsProvider;   // Добавлен для TagLookup
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class MothItemTags extends ItemTagsProvider {

    // Исправлен тип TagLookup на TagsProvider.TagLookup
    public MothItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, modId, existingFileHelper);
    }

    @SafeVarargs
    protected final void tag(TagKey<Item> tag, Item... items) {
        tag(tag).add(items);
    }

    protected void copyBlock(TagKey<Block> blockTag, TagKey<Item> itemTag) {
        copy(blockTag, itemTag);
    }
}