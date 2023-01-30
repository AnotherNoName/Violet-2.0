/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;

public class RotationRenderEvent
extends CancellableEvent {
    private float yaw;
    private float pitch;

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}

