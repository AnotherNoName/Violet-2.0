/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.events;

import me.ninethousand.violet.client.api.event.CancellableEvent;

public class MotionUpdateEvent
extends CancellableEvent {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public double getX() {
        return this.x;
    }

    public void setX(double in) {
        this.x = in;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double in) {
        this.y = in;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double in) {
        this.z = in;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float in) {
        this.yaw = in;
    }

    public void setPitch(float in) {
        this.pitch = in;
    }

    public boolean getOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean in) {
        this.onGround = in;
    }
}

