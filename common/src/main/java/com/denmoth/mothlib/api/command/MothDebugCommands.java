package com.denmoth.mothlib.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Reusable debug command tree for mods using MothLib.
 *
 * <p>Usage in your mod:
 * <pre>{@code
 * MothServices.COMMANDS.registerCommand((dispatcher, ctx, dedicated) ->
 *     MothDebugCommands.register(dispatcher, "mymod", "mymod"));
 * }</pre>
 *
 * <p>This registers: /mymod debug structures|loot|biomes structure|biome
 */
public class MothDebugCommands {

    /** Timeout in seconds for each individual structure locate. */
    private static final long SCAN_TIMEOUT_SECONDS = 15;
    /** Search radius in chunks. */
    private static final int SEARCH_RADIUS = 100;

    /**
     * Registers the debug command subtree under {@code /rootCommand debug}.
     *
     * @param dispatcher the command dispatcher
     * @param rootCommand the literal command name (e.g. "cso", "mymod")
     * @param namespace   the mod namespace used to filter structures (e.g. "cso")
     */
    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            String rootCommand,
            String namespace
    ) {
        SuggestionProvider<CommandSourceStack> suggestModStructures = (ctx, builder) ->
                SharedSuggestionProvider.suggestResource(
                        ctx.getSource().registryAccess().registryOrThrow(Registries.STRUCTURE)
                                .keySet().stream()
                                .filter(rl -> rl.getNamespace().equals(namespace)),
                        builder
                );

        SuggestionProvider<CommandSourceStack> suggestBiomes = (ctx, builder) ->
                SharedSuggestionProvider.suggestResource(
                        ctx.getSource().registryAccess().registryOrThrow(Registries.BIOME).keySet(),
                        builder
                );

        dispatcher.register(Commands.literal(rootCommand)
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("debug")
                        .then(Commands.literal("structures")
                                .executes(ctx -> scanStructures(ctx.getSource(), namespace)))
                        .then(Commands.literal("loot")
                                .executes(ctx -> testLoot(ctx.getSource(), namespace)))
                        .then(Commands.literal("biomes")
                                .then(Commands.literal("structure")
                                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                                .suggests(suggestModStructures)
                                                .executes(ctx -> checkStructureBiomes(ctx.getSource(),
                                                        ResourceLocationArgument.getId(ctx, "id")))))
                                .then(Commands.literal("biome")
                                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                                .suggests(suggestBiomes)
                                                .executes(ctx -> checkBiomeStructures(ctx.getSource(),
                                                        ResourceLocationArgument.getId(ctx, "id"), namespace))))
                        )
                )
        );
    }

    private static int scanStructures(CommandSourceStack source, String namespace) {
        ServerLevel level = source.getLevel();
        BlockPos pos = BlockPos.containing(source.getPosition());

        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        // Collect all structures for this namespace
        List<Map.Entry<ResourceKey<Structure>, Structure>> entries = registry.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(namespace))
                .toList();

        int total = entries.size();
        source.sendSuccess(() -> Component.literal("Scanning " + total + " structures..."), false);

        // Launch ALL searches in parallel on a thread pool
        ExecutorService pool = Executors.newFixedThreadPool(Math.min(total, 8));

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (var entry : entries) {
            ResourceKey<Structure> key = entry.getKey();
            Structure structure = entry.getValue();

            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return level.getChunkSource().getGenerator()
                            .findNearestMapStructure(level, HolderSet.direct(Holder.direct(structure)), pos, SEARCH_RADIUS, false);
                } catch (Exception e) {
                    return null;
                }
            }, pool).orTimeout(SCAN_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .handle((pair, ex) -> {
                String name = key.location().getPath();
                if (ex != null) {
                    source.sendSuccess(() -> Component.literal("§e" + name + " §7- timeout"), false);
                } else if (pair != null) {
                    BlockPos p = pair.getFirst();
                    int dist = (int) Math.sqrt(p.distSqr(pos));
                    String distStr = dist >= 1000 ? (dist / 1000) + "km" : dist + "m";
                    String cmd = "/execute in " + level.dimension().location() + " run tp @s " + p.getX() + " ~ " + p.getZ();
                    Component line = Component.literal("§a" + name + " §f[" + p.getX() + ", " + p.getZ() + "] §7(" + distStr + ")")
                            .withStyle(Style.EMPTY
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Component.literal(cmd))));
                    source.sendSuccess(() -> line, false);
                } else {
                    source.sendSuccess(() -> Component.literal("§7" + name + " §8- not found"), false);
                }
                return null;
            });

            futures.add(future);
        }

        // Wait for all to complete on yet another thread so we don't block the server
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    pool.shutdown();
                    source.sendSuccess(() -> Component.literal("§fDone."), false);
                });

        return 1;
    }

    private static int testLoot(CommandSourceStack source, String namespace) {
        ServerLevel level = source.getLevel();
        BlockPos startPos = BlockPos.containing(source.getPosition());

        var structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        int[] offset = {0};
        int[] skipped = {0};

        for (Map.Entry<ResourceKey<Structure>, Structure> entry : structureRegistry.entrySet()) {
            if (!entry.getKey().location().getNamespace().equals(namespace)) continue;

            String path = entry.getKey().location().getPath();
            BlockPos chestPos = startPos.offset(offset[0] * 2, 0, 0);

            if (level.getBlockState(chestPos).isAir()) {
                level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);
                if (level.getBlockEntity(chestPos) instanceof RandomizableContainerBlockEntity chest) {
                    ResourceLocation lootTableLoc = new ResourceLocation(namespace, path);
                    chest.setLootTable(lootTableLoc, level.getRandom().nextLong());
                    chest.setCustomName(Component.literal("§6" + path));
                    offset[0]++;
                }
            } else {
                skipped[0]++;
            }
        }

        int count = offset[0];
        int skip = skipped[0];
        source.sendSuccess(() -> Component.literal(
                "Placed " + count + " chests" +
                (skip > 0 ? " (" + skip + " skipped)" : "")
        ), false);
        return 1;
    }

    private static int checkStructureBiomes(CommandSourceStack source, ResourceLocation id) {
        ServerLevel level = source.getLevel();
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        if (!registry.containsKey(id)) {
            source.sendFailure(Component.literal("Structure not found: " + id));
            return 0;
        }

        Structure structure = registry.get(id);
        HolderSet<Biome> biomes = structure.biomes();
        long count = biomes.stream().count();

        source.sendSuccess(() -> Component.literal("Biomes for " + id + " (" + count + "):"), false);
        biomes.stream().forEach(holder -> {
            ResourceKey<Biome> key = holder.unwrapKey().orElse(null);
            if (key != null) {
                source.sendSuccess(() -> Component.literal(" - " + key.location()), false);
            }
        });
        return 1;
    }

    private static int checkBiomeStructures(CommandSourceStack source, ResourceLocation biomeId, String namespace) {
        ServerLevel level = source.getLevel();
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        if (!biomeRegistry.containsKey(biomeId)) {
            source.sendFailure(Component.literal("Biome not found: " + biomeId));
            return 0;
        }

        Holder<Biome> biomeHolder = biomeRegistry.getHolder(ResourceKey.create(Registries.BIOME, biomeId)).orElseThrow();

        var matching = structureRegistry.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(namespace))
                .filter(e -> e.getValue().biomes().contains(biomeHolder))
                .toList();

        source.sendSuccess(() -> Component.literal("Structures in " + biomeId + " (" + matching.size() + "):"), false);
        if (matching.isEmpty()) {
            source.sendSuccess(() -> Component.literal(" none"), false);
        } else {
            matching.forEach(entry ->
                    source.sendSuccess(() -> Component.literal(" - " + entry.getKey().location()), false));
        }
        return 1;
    }
}
