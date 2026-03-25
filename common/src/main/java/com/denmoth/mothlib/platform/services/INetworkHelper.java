package com.denmoth.mothlib.platform.services;

import com.denmoth.mothlib.network.IMothPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public interface INetworkHelper {
    enum Side {
        C2S, S2C
    }

    <MSG extends IMothPacket> void registerReceiver(Side side, ResourceLocation id, Function<FriendlyByteBuf, MSG> decoder);
    
    void sendToServer(ResourceLocation id, IMothPacket packet);
    
    void sendToPlayer(ServerPlayer player, ResourceLocation id, IMothPacket packet);
}
