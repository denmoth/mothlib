package com.denmoth.mothlib.api;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;

public class MothVillagerTrades {

    public static void addMapTrade(VillagerTradesEvent event, VillagerProfession profession, int level,
                                   TagKey<Structure> structureTag, String mapNameKey,
                                   MapDecoration.Type iconType, int emeraldCost, int maxUses) {
        if (event.getType() == profession) {
            event.getTrades().get(level).add(new StructureMapTrade(structureTag, mapNameKey, iconType, emeraldCost, maxUses));
        }
    }

    public static void addSimpleTrade(VillagerTradesEvent event, VillagerProfession profession, int level,
                                      ItemStack price, ItemStack result, int maxUses, int xp) {
        if (event.getType() == profession) {
            event.getTrades().get(level).add(new BasicItemListing(price, result, maxUses, xp, 0.05f));
        }
    }

    private static class StructureMapTrade implements VillagerTrades.ItemListing {
        private final TagKey<Structure> destination;
        private final String mapNameKey;
        private final MapDecoration.Type destinationType;
        private final int cost;
        private final int maxUses;

        public StructureMapTrade(TagKey<Structure> destination, String mapNameKey, MapDecoration.Type destinationType, int cost, int maxUses) {
            this.destination = destination;
            this.mapNameKey = mapNameKey;
            this.destinationType = destinationType;
            this.cost = cost;
            this.maxUses = maxUses;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            if (!(trader.level() instanceof ServerLevel level)) return null;

            BlockPos targetPos = level.findNearestMapStructure(destination, trader.blockPosition(), 100, true);

            if (targetPos != null) {
                ItemStack mapStack = MapItem.create(level, targetPos.getX(), targetPos.getZ(), (byte) 2, true, true);
                MapItem.renderBiomePreviewMap(level, mapStack);
                MapItemSavedData.addTargetDecoration(mapStack, targetPos, "+", destinationType);
                mapStack.setHoverName(Component.translatable(mapNameKey));

                return new MerchantOffer(new ItemStack(Items.EMERALD, cost), new ItemStack(Items.COMPASS), mapStack, maxUses, 10, 0.2f);
            }
            return null;
        }
    }
}