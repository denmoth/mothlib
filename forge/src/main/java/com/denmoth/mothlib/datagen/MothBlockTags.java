package com.denmoth.mothlib.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class MothBlockTags extends BlockTagsProvider {
    private final String modId;

    public MothBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
        this.modId = modId;
    }

    protected void tag(TagKey<Block> tag, Block... blocks) {
        tag(tag).add(blocks);
    }

    // TODO: Add support for optional tags if needed for compatibility
}