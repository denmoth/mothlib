package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.api.datagen.MothLanguageBuilder;
import com.denmoth.mothlib.registry.MothBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import org.apache.commons.lang3.StringUtils;

public class MothLanguageProvider extends MothLanguageBuilder {

    public MothLanguageProvider(PackOutput output) {
        super(output, MothLib.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(BuiltInRegistries.BLOCK.getKey(MothBlocks.GROUND_MARKER.get()), "Ground Marker");
        
        for (DyeColor color : DyeColor.values()) {
            String name = StringUtils.capitalize(color.getName().replace("_", " ")) + " Marker";
            addBlock(BuiltInRegistries.BLOCK.getKey(MothBlocks.COLORED_MARKERS.get(color).get()), name);
        }
    }
}
