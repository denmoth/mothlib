package com.denmoth.mothlib.network;

import com.denmoth.mothlib.platform.MothServices;
import com.denmoth.mothlib.platform.services.INetworkHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

/**
 * Thin wrapper around Network implementation. Each packet uses its own {@link ResourceLocation} id
 * ({@code modId/packetName}).
 */
public class MothNetwork {

    private final String modId;

    public MothNetwork(String modId, String version) {
        this(modId);
    }

    public MothNetwork(String modId, String version, String channelPath) {
        this(modId);
    }

    public MothNetwork(String modId) {
        this.modId = modId;
    }

    public ResourceLocation id(String packetName) {
        return new ResourceLocation(modId, packetName);
    }

    public <MSG extends IMothPacket> void register(INetworkHelper.Side side, String packetName, Function<FriendlyByteBuf, MSG> decoder) {
        MothServices.NETWORK.registerReceiver(side, id(packetName), decoder);
    }

    public void sendToServer(String packetName, IMothPacket msg) {
        MothServices.NETWORK.sendToServer(id(packetName), msg);
    }

    public void sendToPlayer(String packetName, IMothPacket msg, ServerPlayer player) {
        MothServices.NETWORK.sendToPlayer(player, id(packetName), msg);
    }

    public void sendToPlayers(String packetName, IMothPacket msg, Iterable<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            sendToPlayer(packetName, msg, player);
        }
    }
}
