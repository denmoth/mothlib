package com.denmoth.mothlib.platform.services;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public interface ICommandHelper {
    /**
     * Registers a command callback.
     * @param callback the action to run when commands are being registered.
     */
    void registerCommand(CommandRegistrationCallback callback);

    @FunctionalInterface
    interface CommandRegistrationCallback {
        void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, boolean dedicated);
    }
}
