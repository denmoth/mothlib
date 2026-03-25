package com.denmoth.mothlib.api.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Строитель для StructureProcessorList.
 * Упрощает создание правил замены блоков (RuleProcessor).
 */
public class MothProcessorBuilder {

    private final BootstapContext<StructureProcessorList> context;
    private final List<StructureProcessor> processors = new ArrayList<>();

    // Временный список правил для текущего RuleProcessor
    private final List<ProcessorRule> currentRules = new ArrayList<>();

    private MothProcessorBuilder(BootstapContext<StructureProcessorList> context) {
        this.context = context;
    }

    public static MothProcessorBuilder create(BootstapContext<StructureProcessorList> context) {
        return new MothProcessorBuilder(context);
    }

    /**
     * Заменяет один блок на другой со 100% шансом.
     */
    public MothProcessorBuilder replace(Block from, Block to) {
        currentRules.add(new ProcessorRule(
                new BlockMatchTest(from),
                AlwaysTrueTest.INSTANCE,
                to.defaultBlockState()
        ));
        return this;
    }

    /**
     * Заменяет блок на другой с определенным шансом (0.0 - 1.0).
     */
    public MothProcessorBuilder replace(Block from, Block to, float chance) {
        currentRules.add(new ProcessorRule(
                new RandomBlockMatchTest(from, chance),
                AlwaysTrueTest.INSTANCE,
                to.defaultBlockState()
        ));
        return this;
    }

    /**
     * Заменяет все блоки из тега на целевой блок.
     */
    public MothProcessorBuilder replace(TagKey<Block> fromTag, Block to) {
        currentRules.add(new ProcessorRule(
                new TagMatchTest(fromTag),
                AlwaysTrueTest.INSTANCE,
                to.defaultBlockState()
        ));
        return this;
    }

    /**
     * Заменяет блок на другой, но сохраняя NBT (если нужно).
     * Обычно для замены сундуков и т.д. лучше использовать специализированные методы,
     * но это база.
     */
    public MothProcessorBuilder replaceState(Block from, BlockState toState) {
        currentRules.add(new ProcessorRule(
                new BlockMatchTest(from),
                AlwaysTrueTest.INSTANCE,
                toState
        ));
        return this;
    }

    /**
     * Завершает текущий набор правил и создает из них RuleProcessor.
     * Нужно вызывать перед добавлением других типов процессоров (если они будут).
     * Но для простоты register() сам вызовет это.
     */
    public MothProcessorBuilder buildRules() {
        if (!currentRules.isEmpty()) {
            processors.add(new RuleProcessor(List.copyOf(currentRules)));
            currentRules.clear();
        }
        return this;
    }

    /**
     * Добавляет кастомный процессор (например, IgnoreProcessor или свой).
     */
    public MothProcessorBuilder addProcessor(StructureProcessor processor) {
        buildRules(); // Сначала упаковываем правила, если они были
        processors.add(processor);
        return this;
    }

    public void register(ResourceKey<StructureProcessorList> key) {
        buildRules(); // Упаковываем оставшиеся правила
        context.register(key, new StructureProcessorList(List.copyOf(processors)));
    }
}