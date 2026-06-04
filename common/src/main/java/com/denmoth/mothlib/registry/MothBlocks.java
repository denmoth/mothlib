package com.denmoth.mothlib.registry;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.api.RegHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MothBlocks {
    public static final RegHelper REGISTRY = new RegHelper(MothLib.MODID);

    public static final Supplier<Block> GROUND_MARKER = registerMarker("ground_marker");

    public static final Map<DyeColor, Supplier<Block>> COLORED_MARKERS = new java.util.HashMap<>();

    static {
        for (DyeColor color : DyeColor.values()) {
            COLORED_MARKERS.put(color, registerMarker(color.getName() + "_marker"));
        }
    }

    public static final Supplier<Block> TREE_MARKER = REGISTRY.block("tree_marker",
            () -> new net.minecraft.world.level.block.RotatedPillarBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().noCollission().noLootTable().instabreak().sound(net.minecraft.world.level.block.SoundType.WOOD)),
            new net.minecraft.world.item.Item.Properties()
    );

    private static Supplier<Block> registerMarker(String name) {
        return REGISTRY.block(name,
                () -> new Block(BlockBehaviour.Properties.of().noCollission().noLootTable().instabreak().sound(net.minecraft.world.level.block.SoundType.GRAVEL)),
                new Item.Properties()
        );
    }

    public static void register() {
        REGISTRY.register();
    }
}
