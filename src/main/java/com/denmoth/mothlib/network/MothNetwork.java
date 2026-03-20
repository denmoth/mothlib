package com.denmoth.mothlib.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class MothNetwork {

    /** Default SimpleChannel name segment (full id is {@code modId:channel}). */
    public static final String DEFAULT_CHANNEL = "main";

    private final SimpleChannel channel;
    private int id = 0;

    public MothNetwork(String modId, String version) {
        this(modId, version, DEFAULT_CHANNEL);
    }

    public MothNetwork(String modId, String version, String channelPath) {
        this.channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(modId, channelPath))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();
    }

    public <MSG extends IMothPacket> void register(Class<MSG> type, Function<net.minecraft.network.FriendlyByteBuf, MSG> decoder, NetworkDirection direction) {
        channel.messageBuilder(type, id++, direction)
                .encoder(IMothPacket::encode)
                .decoder(decoder)
                .consumerMainThread((msg, ctx) -> {
                    ctx.get().enqueueWork(() -> msg.handle(ctx));
                    ctx.get().setPacketHandled(true);
                })
                .add();
    }

    public void sendToServer(IMothPacket msg) {
        channel.send(PacketDistributor.SERVER.noArg(), msg);
    }

    public void sendToPlayer(IMothPacket msg, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public void sendToAll(IMothPacket msg) {
        channel.send(PacketDistributor.ALL.noArg(), msg);
    }
}