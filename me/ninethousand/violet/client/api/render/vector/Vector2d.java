/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.api.render.vector;

import net.minecraft.util.math.Vec3d;

public class Vector2d {
    public static final Vector2d ZERO = new Vector2d(0.0, 0.0);
    public static final Vector2d MAX = new Vector2d(Double.MAX_VALUE, Double.MAX_VALUE);
    public static final Vector2d I = new Vector2d(1.0, 0.0);
    public static final Vector2d J = new Vector2d(0.0, 1.0);
    public final double x;
    public final double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d add(double x, double y) {
        return new Vector2d(this.x + x, this.y + y);
    }

    public Vector2d add(double d) {
        return this.add(d, d);
    }

    public Vector2d add(Vector2d vector2D) {
        return this.add(vector2D.x, vector2D.y);
    }

    public Vector2d subtract(double x, double y) {
        return this.add(-x, -y);
    }

    public Vector2d subtract(double d) {
        return this.subtract(d, d);
    }

    public Vector2d subtract(Vector2d vector2D) {
        return this.subtract(vector2D.x, vector2D.y);
    }

    public Vector2d scale(double x, double y) {
        return new Vector2d(this.x * x, this.y * y);
    }

    public Vector2d scale(double d) {
        return this.scale(d, d);
    }

    public Vector2d scale(Vector2d vector2D) {
        return this.scale(vector2D.x, vector2D.y);
    }

    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vec3d to3D() {
        return new Vec3d(this.x, this.y, 0.0);
    }

    public Vec3d to3D(double z) {
        return new Vec3d(this.x, this.y, z);
    }
}

