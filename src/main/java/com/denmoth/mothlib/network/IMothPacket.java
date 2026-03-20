package com.denmoth.mothlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Интерфейс для всех пакетов.
 * Реализуй его в своем пакете (C2S или S2C).
 */
public interface IMothPacket {

    /**
     * Запись данных в буфер.
     */
    void encode(FriendlyByteBuf buf);

    /**
     * Чтение данных (статический метод в классе пакета должен вызывать конструктор).
     * Здесь этот метод не нужен, но помни, что decoder нужен при регистрации.
     */

    /**
     * Логика пакета.
     * @return true, если пакет был успешно обработан.
     */
    boolean handle(Supplier<NetworkEvent.Context> contextSupplier);
}