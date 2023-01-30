/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;
import net.minecraft.util.math.Vec3d;

public class SpawnParticleEvent
extends CancellableEvent {
    private final int particleId;
    private final Vec3d position;

    public SpawnParticleEvent(int particleId, Vec3d position) {
        this.particleId = particleId;
        this.position = position;
    }

    public int getParticleId() {
        return this.particleId;
    }

    public Vec3d getPosition() {
        return this.position;
    }
}

