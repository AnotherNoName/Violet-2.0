/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.util.minecraft.rotation;

public class Rotation {
    private float yaw;
    private float pitch;
    private final Rotate rotate;
    private final float step;

    public Rotation(float yaw, float pitch, Rotate rotate, float step) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.rotate = rotate;
        this.step = 30.0f;
    }

    public Rotation(float yaw, float pitch, Rotate rotate) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.rotate = rotate;
        this.step = -1.0f;
    }

    public Rotation(float yaw, float pitch) {
        this(yaw, pitch, Rotate.NONE);
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float in) {
        this.yaw = in;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float in) {
        this.pitch = in;
    }

    public Rotate getRotation() {
        return this.rotate;
    }

    public boolean isValid() {
        return !Float.isNaN(this.getYaw()) && !Float.isNaN(this.getPitch());
    }

    public boolean isInstant() {
        return this.step <= 0.0f;
    }

    public float getStep() {
        return this.step;
    }

    public static enum Rotate {
        PACKET,
        CLIENT,
        NONE;

    }
}

