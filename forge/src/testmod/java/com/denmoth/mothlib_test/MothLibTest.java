package com.denmoth.mothlib_test;

import com.denmoth.mothlib.api.RegHelper;
import com.denmoth.mothlib_test.datagen.TestDatagen;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("mothlib_test")
public class MothLibTest {
    public static final String MODID = "mothlib_test";

    // Наш хелпер
    public static final RegHelper REG = new RegHelper(MODID);

    public MothLibTest() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 1. Регистрируем предметы для теста
        // Блок, который будет генерироваться в структуре
        REG.block("test_structure_block", () -> new net.minecraft.world.level.block.Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
        // Предмет для иконки ачивки
        REG.simpleItem("test_icon");

        REG.register();

        // 2. Подключаем Датаген
        modBus.addListener(TestDatagen::gatherData);

        MinecraftForge.EVENT_BUS.register(this);
    }
}