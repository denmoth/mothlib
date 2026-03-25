package com.denmoth.mothlib.platform.fabric;

import com.denmoth.mothlib.platform.services.ICommandHelper;

public class FabricCommandHelper implements ICommandHelper {
    @Override
    public void registerCommand(ICommandHelper.CommandRegistrationCallback callback) {
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> {
            boolean dedicated = environment == net.minecraft.commands.Commands.CommandSelection.DEDICATED;
            callback.register(dispatcher, buildContext, dedicated);
        });
    }
}
