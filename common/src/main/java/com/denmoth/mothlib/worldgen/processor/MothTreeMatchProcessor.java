package com.denmoth.mothlib.worldgen.processor;

import com.denmoth.mothlib.registry.MothBlocks;
import com.denmoth.mothlib.registry.MothStructureProcessorTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class MothTreeMatchProcessor extends StructureProcessor {
    public static final Codec<MothTreeMatchProcessor> CODEC = Codec.unit(MothTreeMatchProcessor::new);
    public static final MothTreeMatchProcessor INSTANCE = new MothTreeMatchProcessor();

    private MothTreeMatchProcessor() {}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings settings) {
        BlockState state = blockInfoGlobal.state();

        if (state.is(MothBlocks.TREE_MARKER.get())) {
            Holder<Biome> biome = level.getBiome(pos);
            Block targetLog = Blocks.OAK_LOG;

            try {
                // Fetch the biome value to inspect features
                Biome b = biome.value();
                var vegetalFeatures = b.getGenerationSettings().features();
                
                // VEGETAL_DECORATION is index 9
                if (vegetalFeatures.size() > net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION.ordinal()) {
                    for (var featureHolder : vegetalFeatures.get(net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION.ordinal())) {
                        net.minecraft.world.level.levelgen.placement.PlacedFeature placedFeature = featureHolder.value();
                        net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?> configuredFeature = placedFeature.feature().value();
                        
                        if (configuredFeature.config() instanceof net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration treeConfig) {
                            BlockState treeState = treeConfig.trunkProvider.getState(settings.getRandom(pos), pos);
                            targetLog = treeState.getBlock();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // Fallback to oak if any error occurs reading features
            }

            // Fallback for missing trees (e.g. deserts)
            if (targetLog == null) {
                targetLog = Blocks.OAK_LOG;
            }

            BlockState newState = targetLog.defaultBlockState();
            if (state.hasProperty(RotatedPillarBlock.AXIS) && newState.hasProperty(RotatedPillarBlock.AXIS)) {
                newState = newState.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
            }

            return new StructureTemplate.StructureBlockInfo(pos, newState, blockInfoGlobal.nbt());
        }

        return blockInfoGlobal;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return MothStructureProcessorTypes.TREE_MATCH.get();
    }
}
