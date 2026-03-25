package com.denmoth.mothlib.api.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Consumer;

public class MothAdvancementBuilder {
    private final Consumer<Advancement> saver;
    private final String modId;
    private final Advancement.Builder builder = Advancement.Builder.advancement();
    private String name;

    private MothAdvancementBuilder(Consumer<Advancement> saver, String modId) {
        this.saver = saver;
        this.modId = modId;
    }

    public static MothAdvancementBuilder create(Consumer<Advancement> saver, String modId) {
        return new MothAdvancementBuilder(saver, modId);
    }

    public MothAdvancementBuilder parent(Advancement parent) {
        builder.parent(parent);
        return this;
    }

    public MothAdvancementBuilder display(ItemLike icon, String name, FrameType frame, boolean toast, boolean announce, boolean hidden) {
        this.name = name;
        builder.display(icon,
                Component.translatable("advancements." + modId + "." + name + ".title"),
                Component.translatable("advancements." + modId + "." + name + ".desc"),
                null, frame, toast, announce, hidden);
        return this;
    }

    public MothAdvancementBuilder task(ItemLike icon, String name) {
        return display(icon, name, FrameType.TASK, true, true, false);
    }

    public MothAdvancementBuilder goal(ItemLike icon, String name) {
        return display(icon, name, FrameType.GOAL, true, true, false);
    }

    public MothAdvancementBuilder challenge(ItemLike icon, String name) {
        return display(icon, name, FrameType.CHALLENGE, true, true, false);
    }

    public MothAdvancementBuilder inStructure(ResourceKey<Structure> structure) {
        builder.addCriterion("find_" + structure.location().getPath(),
                PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(structure)));
        return this;
    }

    public MothAdvancementBuilder killedEntity(EntityType<?> type) {
        builder.addCriterion("killed_" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath(),
                KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(type)));
        return this;
    }

    public MothAdvancementBuilder hasItem(ItemLike item) {
        builder.addCriterion("has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(),
                InventoryChangeTrigger.TriggerInstance.hasItems(item));
        return this;
    }

    public MothAdvancementBuilder inBiome(ResourceKey<net.minecraft.world.level.biome.Biome> biome) {
        builder.addCriterion("in_" + biome.location().getPath(),
                PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(biome)));
        return this;
    }

    public MothAdvancementBuilder interactedWith(net.minecraft.world.level.block.Block block) {
        builder.addCriterion("interacted_with_" + BuiltInRegistries.BLOCK.getKey(block).getPath(),
                ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(
                                net.minecraft.advancements.critereon.BlockPredicate.Builder.block().of(block).build()),
                        ItemPredicate.Builder.item()));
        return this;
    }

    public MothAdvancementBuilder rewardExp(int exp) {
        builder.rewards(net.minecraft.advancements.AdvancementRewards.Builder.experience(exp));
        return this;
    }

    public MothAdvancementBuilder rewardLoot(ResourceLocation lootTable) {
        builder.rewards(net.minecraft.advancements.AdvancementRewards.Builder.loot(lootTable));
        return this;
    }

    public MothAdvancementBuilder or() {
        builder.requirements(RequirementsStrategy.OR);
        return this;
    }

    public Advancement save() {
        // Fabric doesn't use ExistingFileHelper, and on Forge we can bypass it by passing empty string or null 
        // to a custom save method, or simply calling builder.save(saver, String) where supported. 
        // In 1.20.1, builder.save(saver, String) exists.
        return builder.save(saver, modId + ":" + name);
    }
}