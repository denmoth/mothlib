package com.denmoth.mothlib.worldgen.processor;

import com.denmoth.mothlib.registry.MothBlocks;
import com.denmoth.mothlib.registry.MothStructureProcessorTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class RandomWheatProcessor extends StructureProcessor {
    public static final Codec<RandomWheatProcessor> CODEC = Codec.unit(RandomWheatProcessor::new);
    public static final RandomWheatProcessor INSTANCE = new RandomWheatProcessor();

    private RandomWheatProcessor() {}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings settings) {
        BlockState state = blockInfoGlobal.state();
        
        if (state.is(MothBlocks.COLORED_MARKERS.get(DyeColor.LIME).get())) {
            int age = settings.getRandom(pos).nextInt(8); // 0 to 7
            BlockState wheatState = Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, age);
            return new StructureTemplate.StructureBlockInfo(pos, wheatState, null);
        }
        
        return blockInfoGlobal;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return MothStructureProcessorTypes.RANDOM_WHEAT.get();
    }
}
