package com.denmoth.mothlib_test.events;

import com.denmoth.mothlib.api.MothVillagerTrades;
import com.denmoth.mothlib_test.MothLibTest;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MothLibTest.MODID)
public class TestVillagerEvents {

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        // Добавляем продажу карты к деревне библиотекарю на 1 уровне
        MothVillagerTrades.addMapTrade(event, VillagerProfession.LIBRARIAN, 1,
                StructureTags.VILLAGE, "item.mothlib_test.village_map",
                MapDecoration.Type.TARGET_X, 10, 5);

        // Простой трейд
        MothVillagerTrades.addSimpleTrade(event, VillagerProfession.LIBRARIAN, 1,
                Items.EMERALD.getDefaultInstance(), Items.BOOK.getDefaultInstance(), 10, 2);
    }
}