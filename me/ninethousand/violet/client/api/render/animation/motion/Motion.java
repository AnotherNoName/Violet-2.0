/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.animation.motion;

import me.ninethousand.violet.client.api.render.animation.motion.BackMotion;
import me.ninethousand.violet.client.api.render.animation.motion.ElasticMotion;

public interface Motion {
    public static final Motion LINEAR = (t, b, c, d) -> c * t / d + b;
    public static final Motion QUAD_IN = (t, b, c, d) -> c * (t /= d) * t + b;
    public static final Motion QUAD_OUT = (t, b, c, d) -> -c * (t /= d) * (t - 2.0f) + b;
    public static final Motion QUAD_IN_OUT = (t, b, c, d) -> {
        float f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * t * t + b;
        }
        return -c / 2.0f * ((t -= 1.0f) * (t - 2.0f) - 1.0f) + b;
    };
    public static final Motion CUBIC_IN = (t, b, c, d) -> c * (t /= d) * t * t + b;
    public static final Motion CUBIC_OUT = (t, b, c, d) -> {
        t = t / d - 1.0f;
        return c * (t * t * t + 1.0f) + b;
    };
    public static final Motion CUBIC_IN_OUT = (t, b, c, d) -> {
        float f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * t * t * t + b;
        }
        return c / 2.0f * ((t -= 2.0f) * t * t + 2.0f) + b;
    };
    public static final Motion QUARTIC_IN = (t, b, c, d) -> c * (t /= d) * t * t * t + b;
    public static final Motion QUARTIC_OUT = (t, b, c, d) -> {
        t = t / d - 1.0f;
        return -c * (t * t * t * t - 1.0f) + b;
    };
    public static final Motion QUARTIC_IN_OUT = (t, b, c, d) -> {
        float f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * t * t * t * t + b;
        }
        return -c / 2.0f * ((t -= 2.0f) * t * t * t - 2.0f) + b;
    };
    public static final Motion QUINTIC_IN = (t, b, c, d) -> c * (t /= d) * t * t * t * t + b;
    public static final Motion QUINTIC_OUT = (t, b, c, d) -> {
        t = t / d - 1.0f;
        return c * (t * t * t * t * t + 1.0f) + b;
    };
    public static final Motion QUINTIC_IN_OUT = (t, b, c, d) -> {
        float f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * t * t * t * t * t + b;
        }
        return c / 2.0f * ((t -= 2.0f) * t * t * t * t + 2.0f) + b;
    };
    public static final Motion SINE_IN = (t, b, c, d) -> -c * (float)Math.cos((double)(t / d) * 1.5707963267948966) + c + b;
    public static final Motion SINE_OUT = (t, b, c, d) -> c * (float)Math.sin((double)(t / d) * 1.5707963267948966) + b;
    public static final Motion SINE_IN_OUT = (t, b, c, d) -> -c / 2.0f * ((float)Math.cos(Math.PI * (double)t / (double)d) - 1.0f) + b;
    public static final Motion EXPO_IN = (t, b, c, d) -> t == 0.0f ? b : c * (float)Math.pow(2.0, 10.0f * (t / d - 1.0f)) + b;
    public static final Motion EXPO_OUT = (t, b, c, d) -> t == d ? b + c : c * (-((float)Math.pow(2.0, -10.0f * t / d)) + 1.0f) + b;
    public static final Motion EXPO_IN_OUT = (t, b, c, d) -> {
        float f;
        if (t == 0.0f) {
            return b;
        }
        if (t == d) {
            return b + c;
        }
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * (float)Math.pow(2.0, 10.0f * (t - 1.0f)) + b;
        }
        return c / 2.0f * (-((float)Math.pow(2.0, -10.0f * (t -= 1.0f))) + 2.0f) + b;
    };
    public static final Motion CIRC_IN = (t, b, c, d) -> -c * ((float)Math.sqrt(1.0f - (t /= d) * t) - 1.0f) + b;
    public static final Motion CIRC_OUT = (t, b, c, d) -> {
        t = t / d - 1.0f;
        return c * (float)Math.sqrt(1.0f - t * t) + b;
    };
    public static final Motion CIRC_IN_OUT = (t, b, c, d) -> {
        float f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return -c / 2.0f * ((float)Math.sqrt(1.0f - t * t) - 1.0f) + b;
        }
        return c / 2.0f * ((float)Math.sqrt(1.0f - (t -= 2.0f) * t) + 1.0f) + b;
    };
    public static final Motion BOUNCE_IN = (t, b, c, d) -> c - BOUNCE_OUT.ease(d - t, 0.0f, c, d) + b;
    public static final Motion BOUNCE_OUT = (t, b, c, d) -> {
        float f;
        t /= d;
        if (f < 0.36363637f) {
            return c * (7.5625f * t * t) + b;
        }
        if (t < 0.72727275f) {
            return c * (7.5625f * (t -= 0.54545456f) * t + 0.75f) + b;
        }
        if (t < 0.90909094f) {
            return c * (7.5625f * (t -= 0.8181818f) * t + 0.9375f) + b;
        }
        return c * (7.5625f * (t -= 0.95454544f) * t + 0.984375f) + b;
    };
    public static final Motion BOUNCE_IN_OUT = (t, b, c, d) -> {
        if (t < d / 2.0f) {
            return BOUNCE_IN.ease(t * 2.0f, 0.0f, c, d) * 0.5f + b;
        }
        return BOUNCE_OUT.ease(t * 2.0f - d, 0.0f, c, d) * 0.5f + c * 0.5f + b;
    };
    public static final Motion ELASTIC_IN = new ElasticMotion.In();
    public static final Motion ELASTIC_OUT = new ElasticMotion.Out();
    public static final Motion ELASTIC_IN_OUT = new ElasticMotion.InOut();
    public static final Motion BACK_IN = new BackMotion.In();
    public static final Motion BACK_OUT = new BackMotion.Out();
    public static final Motion BACK_IN_OUT = new BackMotion.InOut();

    public float ease(float var1, float var2, float var3, float var4);
}

