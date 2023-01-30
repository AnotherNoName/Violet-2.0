/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.util.misc;

public class Timer {
    private long start;

    public Timer() {
        this.reset();
    }

    public boolean passed(long ms) {
        return this.getTime() > (float)ms;
    }

    public float getTime() {
        return System.currentTimeMillis() - this.start;
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }
}

