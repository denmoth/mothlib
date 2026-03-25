package com.denmoth.mothlib.platform.fabric;

import com.denmoth.mothlib.platform.services.IMothDeferredRegister;
import com.denmoth.mothlib.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T> IMothDeferredRegister<T> create(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        return new FabricDeferredRegister<>(modId, registryKey);
    }

    public static class FabricDeferredRegister<T> implements IMothDeferredRegister<T> {
        private final String modId;
        private final Registry<T> registry;
        private final List<Runnable> entries = new ArrayList<>();

        @SuppressWarnings("unchecked")
        public FabricDeferredRegister(String modId, ResourceKey<? extends Registry<T>> registryKey) {
            this.modId = modId;
            // Fabric uses the built-in registries directly
            this.registry = (Registry<T>) net.minecraft.core.registries.BuiltInRegistries.REGISTRY.get(registryKey.location());
            if (this.registry == null) {
                throw new IllegalArgumentException("Registry not found: " + registryKey.location());
            }
        }

        @Override
        public void register() {
            entries.forEach(Runnable::run);
        }

        @Override
        public <I extends T> Supplier<I> register(String name, Supplier<I> supplier) {
            ResourceLocation id = new ResourceLocation(modId, name);
            // We use a caching supplier so it lazily evaluates once, just like DeferredRegister
            Supplier<I> memoized = new Supplier<I>() {
                private I value;
                @Override
                public I get() {
                    if (value == null) value = supplier.get();
                    return value;
                }
            };
            
            entries.add(() -> Registry.register(registry, id, memoized.get()));
            
            return memoized;
        }
    }
}
