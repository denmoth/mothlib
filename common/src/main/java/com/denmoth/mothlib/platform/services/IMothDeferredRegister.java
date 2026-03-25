package com.denmoth.mothlib.platform.services;

import java.util.function.Supplier;

public interface IMothDeferredRegister<T> {
    void register();
    <I extends T> Supplier<I> register(String name, Supplier<I> supplier);
}
