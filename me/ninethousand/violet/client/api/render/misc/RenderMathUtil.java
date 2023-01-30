/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.misc;

public class RenderMathUtil {
    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double rollover(double num, double min, double max) {
        while (num > max) {
            double diff = max - num;
            if (!(num > max)) continue;
            num = min + Math.abs(diff);
        }
        return num;
    }

    public static float rollover(float num, float min, float max) {
        while (num > max) {
            double diff = max - num;
            if (!(num > max)) continue;
            num = (float)((double)min + Math.abs(diff));
        }
        return num;
    }

    private RenderMathUtil() {
        throw new UnsupportedOperationException();
    }
}

