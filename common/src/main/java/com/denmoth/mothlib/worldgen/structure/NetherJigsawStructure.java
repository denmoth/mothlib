package com.denmoth.mothlib.worldgen.structure;

import com.denmoth.mothlib.registry.MothStructureTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class NetherJigsawStructure extends Structure {

    public static final Codec<NetherJigsawStructure> CODEC = RecordCodecBuilder.<NetherJigsawStructure>mapCodec(instance ->
            instance.group(NetherJigsawStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 7).fieldOf("size").forGetter(structure -> structure.maxDepth),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter),
                    Codec.intRange(-64, 320).fieldOf("scan_start_y").forGetter(structure -> structure.scanStartY)
            ).apply(instance, NetherJigsawStructure::new)).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final int scanStartY;

    public NetherJigsawStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter, int scanStartY) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.scanStartY = scanStartY;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMinBlockX() + 8;
        int z = chunkPos.getMinBlockZ() + 8;

        int startY = this.scanStartY;
        int floorY = -1;

        ChunkGenerator chunkGenerator = context.chunkGenerator();
        RandomState randomState = context.randomState();
        NoiseColumn noiseColumn = chunkGenerator.getBaseColumn(x, z, context.heightAccessor(), randomState);

        // Scan downwards to find the first non-air, non-fluid block
        for (int y = startY; y > context.chunkGenerator().getMinY(); y--) {
            BlockState state = noiseColumn.getBlock(y);
            if (!state.isAir() && state.getFluidState().isEmpty()) {
                floorY = y + 1; // Stand on top of it
                break;
            }
        }

        if (floorY == -1) {
            return Optional.empty(); // No valid floor found
        }

        BlockPos blockPos = new BlockPos(x, floorY, z);

        return JigsawPlacement.addPieces(
                context,
                this.startPool,
                this.startJigsawName,
                this.maxDepth,
                blockPos,
                false,
                this.projectStartToHeightmap,
                this.maxDistanceFromCenter
        );
    }

    @Override
    public StructureType<?> type() {
        return MothStructureTypes.NETHER_JIGSAW.get();
    }
}
