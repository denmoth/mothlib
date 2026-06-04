package com.denmoth.mothlib.datagen;

import com.denmoth.mothlib.MothLib;
import com.denmoth.mothlib.registry.MothBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MothBlockStateProvider extends BlockStateProvider {

    public MothBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MothLib.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Ground Marker - Dirt sides, green top
        Block groundMarker = MothBlocks.GROUND_MARKER.get();
        ModelFile groundModel = models().cubeAll(
                "block/ground_marker",
                new ResourceLocation(MothLib.MODID, "block/ground_marker")
        );
        simpleBlock(groundMarker, groundModel);
        simpleBlockItem(groundMarker, groundModel);

        // Colored Markers - Colored concrete
        for (DyeColor color : DyeColor.values()) {
            Block coloredMarker = MothBlocks.COLORED_MARKERS.get(color).get();
            ModelFile coloredModel = models().cubeAll(
                    "block/" + color.getName() + "_marker",
                    new ResourceLocation("minecraft", "block/" + color.getName() + "_concrete")
            );
            simpleBlock(coloredMarker, coloredModel);
            simpleBlockItem(coloredMarker, coloredModel);
        }

        // Tree Marker - Log axis block with custom texture
        Block treeMarker = MothBlocks.TREE_MARKER.get();
        ModelFile treeModel = models().cubeAll(
                "block/tree_marker",
                new ResourceLocation(MothLib.MODID, "block/tree_marker")
        );
        logBlock((net.minecraft.world.level.block.RotatedPillarBlock) treeMarker);
        simpleBlockItem(treeMarker, treeModel);
    }
}
