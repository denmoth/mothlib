package com.denmoth.mothlib.platform.forge;

import com.denmoth.mothlib.platform.services.ICommandHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeCommandHelper implements ICommandHelper {

    private static final List<CommandRegistrationCallback> callbacks = new ArrayList<>();
    private static boolean registered = false;

    @Override
    public void registerCommand(CommandRegistrationCallback callback) {
        callbacks.add(callback);
        if (!registered) {
            MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
            registered = true;
        }
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        boolean dedicated = event.getCommandSelection() == net.minecraft.commands.Commands.CommandSelection.DEDICATED;
        for (CommandRegistrationCallback callback : callbacks) {
            callback.register(event.getDispatcher(), event.getBuildContext(), dedicated);
        }
    }
}
