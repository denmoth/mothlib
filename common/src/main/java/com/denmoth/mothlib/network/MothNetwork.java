package com.denmoth.mothlib.network;

import com.denmoth.mothlib.platform.MothServices;
import com.denmoth.mothlib.platform.services.INetworkHelper;
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
    private final String version;
    private final String channelPath;

    public MothNetwork(String modId, String version) {
        this(modId, version, "main");
    }

    public MothNetwork(String modId, String version, String channelPath) {
        this.modId = modId;
        this.version = version;
        this.channelPath = channelPath;
    }

    public MothNetwork(String modId) {
        this(modId, "1");
    }

    public ResourceLocation id(String packetName) {
        return new ResourceLocation(modId, packetName);
    }

    public String getVersion() {
        return version;
    }

    public String getChannelPath() {
        return channelPath;
    }

    public <MSG extends IMothPacket> void register(INetworkHelper.Side side, Class<MSG> msgClass, String packetName, Function<FriendlyByteBuf, MSG> decoder) {
        MothServices.NETWORK.registerReceiver(side, id(packetName), msgClass, decoder, version);
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
