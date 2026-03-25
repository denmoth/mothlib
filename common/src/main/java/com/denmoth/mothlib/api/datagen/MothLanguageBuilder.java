package com.denmoth.mothlib.api.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class MothLanguageBuilder implements DataProvider {

    private final Map<String, String> data = new TreeMap<>();
    private final PackOutput output;
    private final String modid;
    private final String locale;

    public MothLanguageBuilder(PackOutput output, String modid, String locale) {
        this.output = output;
        this.modid = modid;
        this.locale = locale;
    }

    protected abstract void addTranslations();

    public void add(String key, String value) {
        if (data.put(key, value) != null) {
            throw new IllegalStateException("Duplicate translation key: " + key);
        }
    }

    public void addBlock(ResourceLocation id, String name) {
        add("block." + id.getNamespace() + "." + id.getPath(), name);
    }

    public void addItem(ResourceLocation id, String name) {
        add("item." + id.getNamespace() + "." + id.getPath(), name);
    }

    public void addEntity(ResourceLocation id, String name) {
        add("entity." + id.getNamespace() + "." + id.getPath(), name);
    }

    public void addTab(String tabId, String name) {
        add("itemGroup." + tabId, name);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        data.clear();
        addTranslations();
        
        if (data.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        JsonObject json = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            json.addProperty(entry.getKey(), entry.getValue());
        }

        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(modid)
                .resolve("lang")
                .resolve(locale + ".json");

        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Languages: " + locale;
    }
}
