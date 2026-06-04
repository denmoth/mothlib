package com.denmoth.mothlib.worldgen.placement;

import com.denmoth.mothlib.api.MothConfigRegistry;
import com.denmoth.mothlib.registry.MothPlacements;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import java.util.Optional;

public class ConfigurableStructurePlacement extends RandomSpreadStructurePlacement {
    public static final Codec<ConfigurableStructurePlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(ConfigurableStructurePlacement::locateOffset),
            StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(ConfigurableStructurePlacement::frequencyReductionMethod),
            Codec.FLOAT.optionalFieldOf("frequency", 1.0F).forGetter(ConfigurableStructurePlacement::frequency),
            Codec.INT.fieldOf("salt").forGetter(ConfigurableStructurePlacement::salt),
            StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(ConfigurableStructurePlacement::exclusionZone),
            Codec.intRange(0, 4096).fieldOf("spacing").forGetter(ConfigurableStructurePlacement::spacing),
            Codec.intRange(0, 4096).fieldOf("separation").forGetter(ConfigurableStructurePlacement::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(ConfigurableStructurePlacement::spreadType),
            Codec.STRING.optionalFieldOf("config_id", "").forGetter(ConfigurableStructurePlacement::getConfigId)
    ).apply(instance, ConfigurableStructurePlacement::new));

    private static final ThreadLocal<Long> CURRENT_SEED = new ThreadLocal<>();
    private final String configId;

    public ConfigurableStructurePlacement(Vec3i offset, FrequencyReductionMethod freqMethod, float freq, int salt, Optional<ExclusionZone> zone, int spacing, int separation, RandomSpreadType type, String configId) {
        super(offset, freqMethod, freq, salt, zone, spacing, separation, type);
        this.configId = configId;
    }

    public ConfigurableStructurePlacement(int spacing, int separation, RandomSpreadType type, int salt, String configId) {
        this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, salt, Optional.empty(), spacing, separation, type, configId);
    }

    private MothConfigRegistry.ConfigEntry getEntry() {
        if (!configId.isEmpty()) {
            return MothConfigRegistry.get(configId);
        }
        return null;
    }

    @Override
    public int spacing() {
        MothConfigRegistry.ConfigEntry entry = getEntry();
        return entry != null ? entry.spacing().get() : super.spacing();
    }

    @Override
    public int separation() {
        MothConfigRegistry.ConfigEntry entry = getEntry();
        if (entry != null) {
            return Math.min(entry.separation().get(), Math.max(0, spacing() - 1));
        }
        return super.separation();
    }

    @Override
    public RandomSpreadType spreadType() {
        MothConfigRegistry.ConfigEntry entry = getEntry();
        if (entry != null) {
            String type = entry.spreadType().get();
            if ("TRIANGULAR".equalsIgnoreCase(type)) return RandomSpreadType.TRIANGULAR;
            return RandomSpreadType.LINEAR;
        }
        return super.spreadType();
    }

    public String getConfigId() { return configId; }
    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState state, int x, int z) {
        long seed = 0L;
        try {
            for (java.lang.reflect.Field field : state.getClass().getDeclaredFields()) {
                if (field.getType() == long.class) {
                    field.setAccessible(true);
                    seed = field.getLong(state);
                    break;
                }
            }
            if (seed == 0L) {
                for (java.lang.reflect.Method method : state.getClass().getDeclaredMethods()) {
                    if (method.getReturnType() == long.class && method.getParameterCount() == 0) {
                        method.setAccessible(true);
                        seed = (long) method.invoke(state);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // fallback
        }
        CURRENT_SEED.set(seed);
        try {
            return super.isStructureChunk(state, x, z);
        } finally {
            CURRENT_SEED.remove();
        }
    }

    @Override
    public int salt() {
        Long seed = CURRENT_SEED.get();
        if (seed != null && seed != 0L) {
            double hash = Math.sin((double)(configId.hashCode() ^ seed)) * 43758.5453123;
            return Math.abs((int) ((hash - Math.floor(hash)) * 1000000000));
        }
        if (!configId.isEmpty()) {
            double hash = Math.sin((double)configId.hashCode()) * 43758.5453123;
            return Math.abs((int) ((hash - Math.floor(hash)) * 1000000000));
        }
        return super.salt();
    }
    @Override public StructurePlacementType<?> type() { return MothPlacements.CONFIGURABLE.get(); }
}