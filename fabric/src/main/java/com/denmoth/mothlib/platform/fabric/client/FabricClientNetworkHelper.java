package com.denmoth.mothlib.platform.fabric.client;

import com.denmoth.mothlib.network.IMothPacket;
import com.denmoth.mothlib.network.IMothPacketContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class FabricClientNetworkHelper {

    public static <MSG extends IMothPacket> void registerClientReceiver(ResourceLocation id, Function<FriendlyByteBuf, MSG> decoder) {
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

    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }
}
