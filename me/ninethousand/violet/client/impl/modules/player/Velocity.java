/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketExplosion
 */
package me.ninethousand.violet.client.impl.modules.player;

import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.impl.events.PacketEvent;
import net.minecraft.network.play.server.SPacketExplosion;

@Module.Manifest(value=Module.Category.PLAYER, description="Makes u not go flying when crystal go brrr")
public class Velocity
extends Module {
    private static Velocity instance;

    @Listener
    public void listen(PacketEvent.Read event) {
        if (event.getPacket() instanceof SPacketExplosion) {
            event.cancel();
        }
    }

    public static Velocity getInstance() {
        if (instance == null) {
            instance = new Velocity();
        }
        return instance;
    }
}

