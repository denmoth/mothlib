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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MothBlockLoot extends BlockLootSubProvider {
    private final DeferredRegister<Block> blockReg;

    public MothBlockLoot(DeferredRegister<Block> blockReg) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        this.blockReg = blockReg;
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return blockReg.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
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