package com.denmoth.mothlib.worldgen.processor;

import com.denmoth.mothlib.registry.MothBlocks;
import com.denmoth.mothlib.registry.MothStructureProcessorTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class MothTerrainMatchProcessor extends StructureProcessor {
    public static final Codec<MothTerrainMatchProcessor> CODEC = Codec.unit(MothTerrainMatchProcessor::new);
    public static final MothTerrainMatchProcessor INSTANCE = new MothTerrainMatchProcessor();

    private MothTerrainMatchProcessor() {}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings settings) {
        BlockState state = blockInfoGlobal.state();
        
        // Check if the block is our ground marker
        if (state.is(MothBlocks.GROUND_MARKER.get())) {
            // Get the block that would naturally generate at this position (or slightly below if it's air)
            // Wait, the structure is replacing the terrain. We need to find the terrain block.
            // In a StructureProcessor, we can read the level.
            // Actually, if the structure is placed in the air, maybe we want the block below it?
            // Usually we just want to match the surface block.
            // Let's get the block state from the level at the current position.
            // If the structure hasn't placed blocks yet, level.getBlockState(pos) might have the original terrain.
            BlockState terrainState = level.getBlockState(pos);
            
            // If it's air or water, maybe we should look down to find the surface?
            // Or maybe just use what's there if it's solid.
            if (terrainState.isAir() || !terrainState.getFluidState().isEmpty()) {
                BlockPos.MutableBlockPos mutablePos = pos.mutable();
                while (mutablePos.getY() > level.getMinBuildHeight()) {
                    mutablePos.move(0, -1, 0);
                    BlockState belowState = level.getBlockState(mutablePos);
                    if (!belowState.isAir() && belowState.getFluidState().isEmpty()) {
                        terrainState = belowState;
                        break;
                    }
                }
            }
            
            return new StructureTemplate.StructureBlockInfo(pos, terrainState, blockInfoGlobal.nbt());
        }
        
        // For colored markers, maybe just remove them or leave them?
        // The user said "цветные маркеры это для процессоров", so they might be used by other processors.
        // We can just return the block as is.
        return blockInfoGlobal;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return MothStructureProcessorTypes.TERRAIN_MATCH.get();
    }
}
