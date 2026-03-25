package com.denmoth.mothlib.platform;

import com.denmoth.mothlib.platform.services.ICommandHelper;
import com.denmoth.mothlib.platform.services.INetworkHelper;
import com.denmoth.mothlib.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class MothServices {
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);
    public static final ICommandHelper COMMANDS = load(ICommandHelper.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
