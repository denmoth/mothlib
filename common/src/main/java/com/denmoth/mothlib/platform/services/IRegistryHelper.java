package com.denmoth.mothlib.platform.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface IRegistryHelper {
    <T> IMothDeferredRegister<T> create(String modId, ResourceKey<? extends Registry<T>> registryKey);
}
