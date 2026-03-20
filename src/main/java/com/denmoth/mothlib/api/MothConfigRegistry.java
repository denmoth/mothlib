package com.denmoth.mothlib.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MothConfigRegistry {
    private static final Map<String, ConfigEntry> REGISTRY = new HashMap<>();

    public record ConfigEntry(
            Supplier<Integer> spacing,
            Supplier<Integer> separation,
            Supplier<String> spreadType
    ) {}

    public static void register(String id, Supplier<Integer> spacing, Supplier<Integer> separation) {
        register(id, spacing, separation, () -> "LINEAR");
    }

    public static void register(String id, Supplier<Integer> spacing, Supplier<Integer> separation, Supplier<String> spreadType) {
        REGISTRY.put(id, new ConfigEntry(spacing, separation, spreadType));
    }

    public static ConfigEntry get(String id) {
        return REGISTRY.get(id);
    }
}