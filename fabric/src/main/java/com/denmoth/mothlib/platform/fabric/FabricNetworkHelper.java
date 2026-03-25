package com.denmoth.mothlib.platform.fabric;

import com.denmoth.mothlib.network.IMothPacket;
import com.denmoth.mothlib.network.IMothPacketContext;
import com.denmoth.mothlib.platform.services.INetworkHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                com.denmoth.mothlib.platform.fabric.client.FabricClientNetworkHelper.registerClientReceiver(id, decoder);
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

    @Override
    public void sendToServer(ResourceLocation id, IMothPacket packet) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            packet.encode(buf);
            com.denmoth.mothlib.platform.fabric.client.FabricClientNetworkHelper.sendToServer(id, buf);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ResourceLocation id, IMothPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ServerPlayNetworking.send(player, id, buf);
    }
}
