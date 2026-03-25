package com.denmoth.mothlib.platform.fabric;

import com.denmoth.mothlib.network.IMothPacket;
import com.denmoth.mothlib.network.IMothPacketContext;
import com.denmoth.mothlib.platform.services.INetworkHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class FabricNetworkHelper implements INetworkHelper {

    @Override
    public <MSG extends IMothPacket> void registerReceiver(Side side, ResourceLocation id, Function<FriendlyByteBuf, MSG> decoder) {
        if (side == Side.S2C) {
            // Client receiver
            // ClientPlayNetworking requires client classes, but we are in common code here.
            // If Fabric is running on a dedicated server, touching ClientPlayNetworking can crash it.
            // To safely support this without splitting client/server in MothLib, we can conditionally load it 
            // or just rely on a separate client initialization path if needed.
            // For now, if someone calls S2C on server, it will crash. That's a known Fabric limitation.
            try {
                Class.forName("net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking");
                registerClientReceiver(id, decoder);
            } catch (ClassNotFoundException e) {
                // Ignore on dedicated server
            }
        } else {
            // Server receiver
            ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, responseSender) -> {
                MSG msg = decoder.apply(buf);
                server.execute(() -> {
                    msg.handle(new IMothPacketContext() {
                        @Override
                        public Player getPlayer() {
                            return player;
                        }

                        @Override
                        public void queue(Runnable runnable) {
                            server.execute(runnable);
                        }
                    });
                });
            });
        }
    }

    private <MSG extends IMothPacket> void registerClientReceiver(ResourceLocation id, Function<FriendlyByteBuf, MSG> decoder) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, responseSender) -> {
            MSG msg = decoder.apply(buf);
            client.execute(() -> {
                msg.handle(new IMothPacketContext() {
                    @Override
                    public Player getPlayer() {
                        return client.player;
                    }

                    @Override
                    public void queue(Runnable runnable) {
                        client.execute(runnable);
                    }
                });
            });
        });
    }

    @Override
    public void sendToServer(ResourceLocation id, IMothPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(id, buf);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ResourceLocation id, IMothPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ServerPlayNetworking.send(player, id, buf);
    }
}
