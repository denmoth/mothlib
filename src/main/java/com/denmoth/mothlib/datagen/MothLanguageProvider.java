package com.denmoth.mothlib.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

// Просто удобная обертка
public abstract class MothLanguageProvider extends LanguageProvider {
    public MothLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    // Добавляем метод для быстрого перевода ключей креативных вкладок
    public void addTab(String tabId, String name) {
        add("itemGroup." + tabId, name);
    }
}