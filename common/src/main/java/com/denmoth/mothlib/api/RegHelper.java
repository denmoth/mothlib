package com.denmoth.mothlib.api;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public class RegHelper {
    private final String modId;
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;
    private final DeferredRegister<SoundEvent> sounds;
    private final DeferredRegister<EntityType<?>> entities;
    private final DeferredRegister<StructureType<?>> structureTypes;

    public RegHelper(String modId) {
        this.modId = modId;
        this.blocks = DeferredRegister.create(modId, Registries.BLOCK);
        this.items = DeferredRegister.create(modId, Registries.ITEM);
        this.sounds = DeferredRegister.create(modId, Registries.SOUND_EVENT);
        this.entities = DeferredRegister.create(modId, Registries.ENTITY_TYPE);
        this.structureTypes = DeferredRegister.create(modId, Registries.STRUCTURE_TYPE);
    }

    /** Call once from mod initialization (Architectury registers all deferred entries). */
    public void register() {
        blocks.register();
        items.register();
        sounds.register();
        entities.register();
        structureTypes.register();
    }

    public <T extends Block> RegistrySupplier<T> block(String name, Supplier<T> block) {
        return block(name, block, new Item.Properties());
    }

    public <T extends Block> RegistrySupplier<T> block(String name, Supplier<T> block, Item.Properties props) {
        RegistrySupplier<T> ret = blocks.register(name, block);
        items.register(name, () -> new BlockItem(ret.get(), props));
        return ret;
    }

    public <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item) {
        return items.register(name, item);
    }

    public RegistrySupplier<Item> simpleItem(String name) {
        return item(name, () -> new Item(new Item.Properties()));
    }

    public RegistrySupplier<SoundEvent> sound(String name) {
        return sounds.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
    }

    public <T extends Entity> RegistrySupplier<EntityType<T>> entity(String name, EntityType.Builder<T> builder) {
        return entities.register(name, () -> builder.build(name));
    }

    public <S extends Structure> RegistrySupplier<StructureType<S>> structureType(String name, Codec<S> codec) {
        return structureTypes.register(name, () -> () -> codec);
    }
}
