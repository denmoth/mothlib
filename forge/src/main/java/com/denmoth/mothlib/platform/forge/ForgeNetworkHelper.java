package com.denmoth.mothlib.platform.forge;

import com.denmoth.mothlib.network.IMothPacket;
import com.denmoth.mothlib.network.IMothPacketContext;
import com.denmoth.mothlib.platform.services.INetworkHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ForgeNetworkHelper implements INetworkHelper {

    private static final Map<String, SimpleChannel> CHANNELS = new HashMap<>();
    private static int packetId = 0;

    private SimpleChannel getOrCreateChannel(String modId, String version) {
        return CHANNELS.computeIfAbsent(modId, id -> NetworkRegistry.newSimpleChannel(
                new ResourceLocation(id, "main"),
                () -> version,
                version::equals,
                version::equals
        ));
    }

    @Override
    public <MSG extends IMothPacket> void registerReceiver(Side side, ResourceLocation id, Class<MSG> msgClass, Function<FriendlyByteBuf, MSG> decoder, String version) {
        SimpleChannel channel = getOrCreateChannel(id.getNamespace(), version);
        channel.registerMessage(packetId++, msgClass, IMothPacket::encode, decoder, (msg, ctxSupplier) -> {
            NetworkEvent.Context ctx = ctxSupplier.get();
            ctx.enqueueWork(() -> {
                msg.handle(new IMothPacketContext() {
                    @Override
                    public Player getPlayer() {
                        return ctx.getSender();
                    }

                    @Override
                    public void queue(Runnable runnable) {
                        ctx.enqueueWork(runnable);
                    }
                });
            });
            ctx.setPacketHandled(true);
        });
    }

    @Override
    public void sendToServer(ResourceLocation id, IMothPacket packet) {
        SimpleChannel channel = CHANNELS.get(id.getNamespace());
        if (channel != null) {
            channel.sendToServer(packet);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ResourceLocation id, IMothPacket packet) {
        SimpleChannel channel = CHANNELS.get(id.getNamespace());
        if (channel != null) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }
}
