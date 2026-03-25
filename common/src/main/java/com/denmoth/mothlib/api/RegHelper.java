package com.denmoth.mothlib.api;

import com.mojang.serialization.Codec;
import com.denmoth.mothlib.platform.MothServices;
import com.denmoth.mothlib.platform.services.IMothDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public class RegHelper {
    private final String modId;
    private final IMothDeferredRegister<Block> blocks;
    private final IMothDeferredRegister<Item> items;
    private final IMothDeferredRegister<SoundEvent> sounds;
    private final IMothDeferredRegister<EntityType<?>> entities;
    private final IMothDeferredRegister<StructureType<?>> structureTypes;
    private final IMothDeferredRegister<BlockEntityType<?>> blockEntities;
    private final IMothDeferredRegister<MenuType<?>> menus;

    public RegHelper(String modId) {
        this.modId = modId;
        this.blocks = MothServices.REGISTRY.create(modId, Registries.BLOCK);
        this.items = MothServices.REGISTRY.create(modId, Registries.ITEM);
        this.sounds = MothServices.REGISTRY.create(modId, Registries.SOUND_EVENT);
        this.entities = MothServices.REGISTRY.create(modId, Registries.ENTITY_TYPE);
        this.structureTypes = MothServices.REGISTRY.create(modId, Registries.STRUCTURE_TYPE);
        this.blockEntities = MothServices.REGISTRY.create(modId, Registries.BLOCK_ENTITY_TYPE);
        this.menus = MothServices.REGISTRY.create(modId, Registries.MENU);
    }

    /** Call once from mod initialization. */
    public void register() {
        blocks.register();
        items.register();
        sounds.register();
        entities.register();
        structureTypes.register();
        blockEntities.register();
        menus.register();
    }

    public <T extends Block> Supplier<T> block(String name, Supplier<T> block) {
        return block(name, block, new Item.Properties());
    }

    public <T extends Block> Supplier<T> block(String name, Supplier<T> block, Item.Properties props) {
        Supplier<T> ret = blocks.register(name, block);
        items.register(name, () -> new BlockItem(ret.get(), props));
        return ret;
    }

    public <T extends Item> Supplier<T> item(String name, Supplier<T> item) {
        return items.register(name, item);
    }

    public Supplier<Item> simpleItem(String name) {
        return item(name, () -> new Item(new Item.Properties()));
    }

    public Supplier<SoundEvent> sound(String name) {
        return sounds.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
    }

    public <T extends Entity> Supplier<EntityType<T>> entity(String name, EntityType.Builder<T> builder) {
        return entities.register(name, () -> builder.build(name));
    }

    public <S extends Structure> Supplier<StructureType<S>> structureType(String name, Codec<S> codec) {
        return structureTypes.register(name, () -> () -> codec);
    }

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntity(String name, Supplier<BlockEntityType<T>> blockEntityType) {
        return blockEntities.register(name, blockEntityType);
    }

    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> menu(String name, Supplier<MenuType<T>> menuType) {
        return menus.register(name, menuType);
    }
}
