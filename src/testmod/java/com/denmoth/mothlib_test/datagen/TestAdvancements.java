package com.denmoth.mothlib_test.datagen;

import com.denmoth.mothlib.api.datagen.MothAdvancementBuilder;
import com.denmoth.mothlib_test.MothLibTest;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class TestAdvancements {
    public static void generate(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {

        // Корневая ачивка
        Advancement root = MothAdvancementBuilder.create(saver, existingFileHelper, MothLibTest.MODID)
                .task(Items.DIAMOND_PICKAXE, "root") // Иконка и ID
                .save();

        // Ачивка за нахождение структуры
        MothAdvancementBuilder.create(saver, existingFileHelper, MothLibTest.MODID)
                .parent(root)
                .goal(Items.COMPASS, "find_structure")
                .inStructure(TestWorldGen.STRUCT_MAIN) // Ссылка на ключ структуры
                .save();
    }
}