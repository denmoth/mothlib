package com.denmoth.mothlib.platform.forge;

import com.denmoth.mothlib.platform.services.IMothDeferredRegister;
import com.denmoth.mothlib.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public <T> IMothDeferredRegister<T> create(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        return new ForgeDeferredRegister<>(modId, registryKey);
    }

    public static class ForgeDeferredRegister<T> implements IMothDeferredRegister<T> {
        private final DeferredRegister<T> internal;

        public ForgeDeferredRegister(String modId, ResourceKey<? extends Registry<T>> registryKey) {
            this.internal = DeferredRegister.create(registryKey, modId);
        }

        @Override
        public void register() {
            internal.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        @Override
        public <I extends T> Supplier<I> register(String name, Supplier<I> supplier) {
            return internal.register(name, supplier);
        }
    }
}
