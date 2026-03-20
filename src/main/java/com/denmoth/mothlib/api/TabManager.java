package com.denmoth.mothlib.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TabManager {
    private final DeferredRegister<CreativeModeTab> tabs;

    public TabManager(String modId) {
        this.tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
    }

    public void register(IEventBus bus) {
        tabs.register(bus);
    }

    public MothTab createTab(String id, Supplier<ItemStack> icon) {
        MothTab tab = new MothTab(id, icon);
        tabs.register(id, tab::build);
        return tab;
    }

    public static class MothTab {
        private final String id;
        private final Supplier<ItemStack> icon;
        private final List<Supplier<? extends ItemLike>> items = new ArrayList<>();

        private MothTab(String id, Supplier<ItemStack> icon) {
            this.id = id;
            this.icon = icon;
        }

        public <T extends ItemLike> RegistryObject<T> add(RegistryObject<T> item) {
            items.add(item);
            return item;
        }

        private CreativeModeTab build() {
            return CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + id))
                    .icon(icon)
                    .displayItems((params, output) -> items.forEach(s -> output.accept(s.get())))
                    .build();
        }
    }
}