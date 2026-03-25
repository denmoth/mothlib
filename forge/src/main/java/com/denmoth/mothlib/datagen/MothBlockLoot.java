package com.denmoth.mothlib.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.Set;
import java.util.function.Supplier;

public abstract class MothBlockLoot extends BlockLootSubProvider {
    private final Supplier<Iterable<Block>> knownBlocks;

    public MothBlockLoot(Supplier<Iterable<Block>> knownBlocks) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        this.knownBlocks = knownBlocks;
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks.get();
    }

    public void dropOre(Block block, Item drop, int min, int max) {
        add(block, createSilkTouchDispatchTable(block,
                this.applyExplosionDecay(block,
                        LootItem.lootTableItem(drop)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                )
        ));
    }
}