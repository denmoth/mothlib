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

    private final String configId;
    private MothConfigRegistry.ConfigEntry cachedEntry;

    public ConfigurableStructurePlacement(Vec3i offset, FrequencyReductionMethod freqMethod, float freq, int salt, Optional<ExclusionZone> zone, int spacing, int separation, RandomSpreadType type, String configId) {
        super(offset, freqMethod, freq, salt, zone, spacing, separation, type);
        this.configId = configId;
    }

    public ConfigurableStructurePlacement(int spacing, int separation, RandomSpreadType type, int salt, String configId) {
        this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, salt, Optional.empty(), spacing, separation, type, configId);
    }

    private MothConfigRegistry.ConfigEntry getEntry() {
        if (cachedEntry == null && !configId.isEmpty()) {
            cachedEntry = MothConfigRegistry.get(configId);
        }
        return cachedEntry;
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
    @Override public StructurePlacementType<?> type() { return MothPlacements.CONFIGURABLE.get(); }
}