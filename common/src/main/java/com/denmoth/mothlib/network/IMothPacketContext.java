package com.denmoth.mothlib.network;

import net.minecraft.world.entity.player.Player;

public interface IMothPacketContext {
    Player getPlayer();
    void queue(Runnable runnable);
}
