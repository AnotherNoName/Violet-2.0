/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketEvent
extends CancellableEvent {
    private final Packet<?> packet;

    private PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public static class Write
    extends PacketEvent {
        public Write(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Read
    extends PacketEvent {
        public Read(Packet<?> packet) {
            super(packet);
        }
    }
}

