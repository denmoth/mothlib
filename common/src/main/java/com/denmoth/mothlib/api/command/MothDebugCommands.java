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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    /** Timeout in seconds for the structure locate scan. */
    private static final long SCAN_TIMEOUT_SECONDS = 10;

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

        source.sendSuccess(() -> Component.literal(
                "§8┌────────────────────────────────┐"), false);
        source.sendSuccess(() -> Component.literal(
                "§6 [SCAN] Structure Scan  §7[" + namespace.toUpperCase() + "]  r=50ch  t=" + SCAN_TIMEOUT_SECONDS + "s"), false);
        source.sendSuccess(() -> Component.literal(
                "§8└────────────────────────────────┘"), false);

        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        CompletableFuture.runAsync(() -> {
            registry.entrySet().stream()
                    .filter(e -> e.getKey().location().getNamespace().equals(namespace))
                    .forEach(entry -> {
                        ResourceKey<Structure> key = entry.getKey();
                        Structure structure = entry.getValue();
                        try {
                            CompletableFuture<com.mojang.datafixers.util.Pair<BlockPos, Holder<Structure>>> future =
                                    CompletableFuture.supplyAsync(() ->
                                            level.getChunkSource().getGenerator()
                                                    .findNearestMapStructure(level, HolderSet.direct(Holder.direct(structure)), pos, 50, false)
                                    );

                            var pair = future.get(SCAN_TIMEOUT_SECONDS, TimeUnit.SECONDS);

                            if (pair != null) {
                                BlockPos p = pair.getFirst();
                                int dist = (int) Math.sqrt(p.distSqr(pos));
                                String distStr = dist >= 1000 ? (dist / 1000) + "k" : dist + "";
                                String cmd = "/tp @s " + p.getX() + " ~ " + p.getZ();
                                Component coords = Component.literal("§b[" + p.getX() + ", " + p.getZ() + "]")
                                        .withStyle(Style.EMPTY
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                        Component.literal("§eClick to suggest /tp command")))
                                                .withUnderlined(true)
                                                .withBold(false));
                                source.sendSuccess(() -> Component.literal(
                                        "§a ✓ §f" + key.location().getPath() + " §7(" + distStr + "m) → ").append(coords), false);
                            } else {
                                source.sendSuccess(() -> Component.literal(
                                        "§7 · " + key.location().getPath() + " §8— not in range"), false);
                            }
                        } catch (TimeoutException e) {
                            source.sendSuccess(() -> Component.literal(
                                    "§e [T] " + key.location().getPath() + " §8— timeout (>" + SCAN_TIMEOUT_SECONDS + "s)"), false);
                        } catch (Exception e) {
                            source.sendSuccess(() -> Component.literal(
                                    "§c ✗ " + key.location().getPath() + "§8: " + e.getMessage()), false);
                        }
                    });

            source.sendSuccess(() -> Component.literal("§8─────── §6Scan complete §8───────"), false);
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
                "§8───── §6Loot Test §8─────\n" +
                "§a ✓ §fPlaced §b" + count + "§f chests" +
                (skip > 0 ? "  §7(" + skip + " skipped — not air)" : "")
        ), false);
        return 1;
    }

    private static int checkStructureBiomes(CommandSourceStack source, ResourceLocation id) {
        ServerLevel level = source.getLevel();
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        if (!registry.containsKey(id)) {
            source.sendFailure(Component.literal("§cStructure not found: §f" + id));
            return 0;
        }

        Structure structure = registry.get(id);
        HolderSet<Biome> biomes = structure.biomes();
        long count = biomes.stream().count();

        source.sendSuccess(() -> Component.literal(
                "§8─── §6Biomes for §b" + id + " §7(" + count + ") §8───"), false);
        biomes.stream().forEach(holder -> {
            ResourceKey<Biome> key = holder.unwrapKey().orElse(null);
            if (key != null) {
                source.sendSuccess(() -> Component.literal(" §8· §a" + key.location()), false);
            }
        });
        return 1;
    }

    private static int checkBiomeStructures(CommandSourceStack source, ResourceLocation biomeId, String namespace) {
        ServerLevel level = source.getLevel();
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        if (!biomeRegistry.containsKey(biomeId)) {
            source.sendFailure(Component.literal("§cBiome not found: §f" + biomeId));
            return 0;
        }

        Holder<Biome> biomeHolder = biomeRegistry.getHolder(ResourceKey.create(Registries.BIOME, biomeId)).orElseThrow();

        var matching = structureRegistry.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(namespace))
                .filter(e -> e.getValue().biomes().contains(biomeHolder))
                .toList();

        source.sendSuccess(() -> Component.literal(
                "§8─── §6Structures in §b" + biomeId + " §7(" + matching.size() + ") §8───"), false);
        if (matching.isEmpty()) {
            source.sendSuccess(() -> Component.literal("§7 none"), false);
        } else {
            matching.forEach(entry ->
                    source.sendSuccess(() -> Component.literal(" §8· §a" + entry.getKey().location()), false));
        }
        return 1;
    }
}
