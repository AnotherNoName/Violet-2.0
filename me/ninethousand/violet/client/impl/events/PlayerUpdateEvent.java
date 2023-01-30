/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.impl.events;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class PlayerUpdateEvent {
    private Vec3d position;
    private Vec2f rotation;
    private boolean onGround;

    public PlayerUpdateEvent() {
    }

    public PlayerUpdateEvent(Vec3d position, Vec2f rotation, boolean onGround) {
        this.position = position;
        this.rotation = rotation;
        this.onGround = onGround;
    }

    public Vec3d getPosition() {
        return this.position;
    }

    public PlayerUpdateEvent setPosition(Vec3d position) {
        this.position = position;
        return this;
    }

    public Vec2f getRotation() {
        return this.rotation;
    }

    public PlayerUpdateEvent setRotation(Vec2f rotation) {
        this.rotation = rotation;
        return this;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public PlayerUpdateEvent setOnGround(boolean onGround) {
        this.onGround = onGround;
        return this;
    }

    public static class Post
    extends PlayerUpdateEvent {
    }

    public static class Pre
    extends PlayerUpdateEvent {
    }
}

