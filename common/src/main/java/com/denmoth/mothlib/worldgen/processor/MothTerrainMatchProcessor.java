package com.denmoth.mothlib.worldgen.processor;

import com.denmoth.mothlib.registry.MothBlocks;
import com.denmoth.mothlib.registry.MothStructureProcessorTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
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
            // Find the highest block on the surface at the column.
            int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) - 1;
            
            // If the structure is above the surface, we might be placing it in the air. 
            // The heightmap returns the top block (e.g. grass). 
            // We use the block slightly below if the calculated surface is valid.
            BlockPos targetPos = new BlockPos(pos.getX(), Math.max(surfaceY, level.getMinBuildHeight()), pos.getZ());
            BlockState terrainState = level.getBlockState(targetPos);
            
            return new StructureTemplate.StructureBlockInfo(pos, terrainState, blockInfoGlobal.nbt());
        }
        
        return blockInfoGlobal;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return MothStructureProcessorTypes.TERRAIN_MATCH.get();
    }
}
