/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.util.misc;

public class MathUtil {
    public static int pickLarger(int i1, int i2) {
        if (i1 < i2) {
            return i2;
        }
        return i1;
    }

    public static int pickLesser(int i1, int i2) {
        if (i1 > i2) {
            return i2;
        }
        return i1;
    }

    public static double clampMin(int in, int min) {
        if (in > min) {
            return in;
        }
        return min;
    }

    public static double round(double in, int decimals) {
        return (double)Math.round(in * Math.pow(10.0, decimals)) / Math.pow(10.0, decimals);
    }
}

