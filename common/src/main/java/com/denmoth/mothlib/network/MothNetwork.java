package com.denmoth.mothlib.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

/**
 * Thin wrapper around Architectury {@link NetworkManager}. Each packet uses its own {@link ResourceLocation} id
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

    public <MSG extends IMothPacket> void register(NetworkManager.Side side, String packetName, Function<FriendlyByteBuf, MSG> decoder) {
        ResourceLocation pid = id(packetName);
        NetworkManager.registerReceiver(side, pid, (buf, ctx) -> {
            MSG msg = decoder.apply(buf);
            ctx.queue(() -> msg.handle(ctx));
        });
    }

    public void sendToServer(String packetName, IMothPacket msg) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        msg.encode(buf);
        NetworkManager.sendToServer(id(packetName), buf);
    }

    public void sendToPlayer(String packetName, IMothPacket msg, ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        msg.encode(buf);
        NetworkManager.sendToPlayer(player, id(packetName), buf);
    }

    public void sendToPlayers(String packetName, IMothPacket msg, Iterable<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            sendToPlayer(packetName, msg, player);
        }
    }
}
