package com.denmoth.mothlib.api;

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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.mojang.serialization.Codec;

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
        this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
        this.items = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        this.sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modId);
        this.entities = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, modId);
        this.structureTypes = DeferredRegister.create(Registries.STRUCTURE_TYPE, modId);
    }

    public void register(IEventBus bus) {
        blocks.register(bus);
        items.register(bus);
        sounds.register(bus);
        entities.register(bus);
        structureTypes.register(bus);
    }

    public <T extends Block> RegistryObject<T> block(String name, Supplier<T> block) {
        return block(name, block, new Item.Properties());
    }

    public <T extends Block> RegistryObject<T> block(String name, Supplier<T> block, Item.Properties props) {
        RegistryObject<T> ret = blocks.register(name, block);
        items.register(name, () -> new BlockItem(ret.get(), props));
        return ret;
    }

    public <T extends Item> RegistryObject<T> item(String name, Supplier<T> item) {
        return items.register(name, item);
    }

    public RegistryObject<Item> simpleItem(String name) {
        return item(name, () -> new Item(new Item.Properties()));
    }

    public RegistryObject<SoundEvent> sound(String name) {
        return sounds.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
    }

    public <T extends Entity> RegistryObject<EntityType<T>> entity(String name, EntityType.Builder<T> builder) {
        return entities.register(name, () -> builder.build(name));
    }

    public <S extends Structure> RegistryObject<StructureType<S>> structureType(String name, Codec<S> codec) {
        return structureTypes.register(name, () -> () -> codec);
    }
}