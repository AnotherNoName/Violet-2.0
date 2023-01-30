/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.render.misc;

public class Stopwatch {
    private long start;

    public Stopwatch() {
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

