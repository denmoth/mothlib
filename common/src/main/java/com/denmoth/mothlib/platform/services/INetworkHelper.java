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

    <MSG extends IMothPacket> void registerReceiver(Side side, ResourceLocation id, Class<MSG> msgClass, Function<FriendlyByteBuf, MSG> decoder, String version);
    
    void sendToServer(ResourceLocation id, IMothPacket packet);
    
    void sendToPlayer(ServerPlayer player, ResourceLocation id, IMothPacket packet);
}
