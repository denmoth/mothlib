package com.denmoth.mothlib.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

public interface IMothPacket {

    void encode(FriendlyByteBuf buf);

    void handle(NetworkManager.PacketContext context);
}
