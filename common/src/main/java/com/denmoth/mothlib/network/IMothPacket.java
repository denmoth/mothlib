package com.denmoth.mothlib.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IMothPacket {

    void encode(FriendlyByteBuf buf);

    void handle(IMothPacketContext context);
}
