//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package me.ninethousand.violet.client.api.render.vector;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class VectorUtil {
    public static Vec2f plus(Vec2f vec2f, float f) {
        return new Vec2f(vec2f.x + f, vec2f.y + f);
    }

    public static Vec2f minus(Vec2f vec2f, float f) {
        return VectorUtil.plus(vec2f, -f);
    }

    public static Vec2f plus(Vec2f vec2f, float x1, float y1) {
        return new Vec2f(vec2f.x + x1, vec2f.y + y1);
    }

    public static Vec2f minus(Vec2f vec2f, float x1, float y1) {
        return VectorUtil.plus(vec2f, -x1, -y1);
    }

    public static Vec2f scale(Vec2f vec2f, float scale) {
        return new Vec2f(vec2f.x * scale, vec2f.y * scale);
    }

    public static Vec2f scale(Vec2f vec2f, float scaleX, float scaleY) {
        return new Vec2f(vec2f.x * scaleX, vec2f.y * scaleY);
    }

    public static float length(Vec2f vec2f) {
        return (float)Math.sqrt(vec2f.x * vec2f.x + vec2f.y * vec2f.y);
    }

    public static Vec3d to3D(Vec2f vec2f) {
        return new Vec3d((double)vec2f.x, (double)vec2f.y, 0.0);
    }

    public static Vec3d to3D(Vec2f vec2f, double z) {
        return new Vec3d((double)vec2f.x, (double)vec2f.y, z);
    }

    private VectorUtil() {
        throw new UnsupportedOperationException();
    }
}

